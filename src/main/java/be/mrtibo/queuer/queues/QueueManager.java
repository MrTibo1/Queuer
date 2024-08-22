package be.mrtibo.queuer.queues;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.commands.QueuerCommandHandler;
import be.mrtibo.queuer.commands.QueuerCompleter;
import be.mrtibo.queuer.events.BlockBreakEvent;
import be.mrtibo.queuer.events.PlayerQuitEvent;
import be.mrtibo.queuer.events.SignChangeEvent;
import be.mrtibo.queuer.events.SignInteractEvent;
import be.mrtibo.queuer.schedules.QueuePositions;
import be.mrtibo.queuer.signs.QueueSign;
import org.bukkit.block.Sign;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QueueManager {

    private final List<Queue> queues = new ArrayList<>();
    File saveFile = new File(Queuer.getInstance().getDataFolder() + File.separator + "queues.yml");
    QueuePositions positionsScheduler;

    public QueueManager() {
        PluginCommand command = Queuer.getInstance().getServer().getPluginCommand("queuer");
        command.setExecutor(new QueuerCommandHandler());
        command.setTabCompleter(new QueuerCompleter());
        new SignChangeEvent();
        new BlockBreakEvent();
        new SignInteractEvent();
        new PlayerQuitEvent();
        positionsScheduler = new QueuePositions();
        positionsScheduler.start();
    }

    public void onShutdown() {
        positionsScheduler.stop();
        save();
    }

    public void registerQueue(Queue queue) {
        if (isRegistered(queue.getName())) queues.remove(getQueue(queue.getName()));
        queues.add(queue);
        if(queue.getMode() == QueueMode.AUTO) queue.startTimer();
    }

    public void removeQueue(Queue queue) {
        queue.getSigns().forEach((sign) -> {
            sign.getState().getBlock().breakNaturally();
        });
        queues.remove(queue);
        save();
    }

    public boolean isRegistered(String id) {
        return getQueue(id) != null;
    }

    public Queue getQueue(String name) {
        for (Queue queue : queues) {
            if(Objects.equals(queue.getName(), name)) {
                return queue;
            }
        }
        return null;
    }

    public List<Queue> getQueues() {
        return queues;
    }

    public QueueSign getQueueSign(Sign sign) {
        for(Queue queue : getQueues()){
            for (QueueSign qs : queue.getSigns()) {
                if(qs.getState().getLocation().equals(sign.getLocation())) {
                    return qs;
                }
            }
        }
        return null;
    }

    public void loadSavedQueues() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(saveFile);
        for (String key : config.getKeys(false)) {
            try {
                Map<String, Object> map = config.getConfigurationSection(key).getValues(true);
                registerQueue(Queue.deserialize(map));
            } catch (Exception ex) {
                Queuer.getInstance().getLogger().severe("Couldn't load queue \"" + key + "\" from file. " + ex.getMessage());
            }
        }
    }

    public void save() {
        if (!saveFile.exists()) {
            try {
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
            } catch (IOException e) {
                Queuer.getInstance().getLogger().severe("Failed to create queues.yml data file");
            }
        }

        YamlConfiguration config = new YamlConfiguration();

        for (Queue q : this.queues) {
            try {
                config.set(String.valueOf(q.getName()), q.serialize());
            } catch (Exception ex) {
                Queuer.getInstance().getLogger().severe("Couldn't save queue \"" + q.getName() + "\" to file. " + ex.getMessage());
            }
        }

        try {
            config.save(saveFile);
        } catch (IOException e) {
            Queuer.getInstance().getLogger().severe("Couldn't save queue data. %s".formatted(e.getMessage()));
        }
    }

    public Queue getQueue(UUID uuid) {
        for( Queue queue : getQueues()) {
            if (queue.getMembers().contains(uuid)) {
                return queue;
            }
        }
        return null;
    }

}

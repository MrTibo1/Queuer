package be.mrtibo.queuer.queues;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.config.Configuration;
import be.mrtibo.queuer.signs.QueueSign;
import be.mrtibo.queuer.util.ComponentUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.*;

public class Queue {

    private final String name;
    private int limit;
    private QueueMode mode;
    private LinkedList<UUID> members = new LinkedList<>();
    private List<QueueSign> signs = new ArrayList<>();
    private Location destination;
    private int interval = 20;
    private int timer = 0;
    private int taskId = -1;

    public Queue(String name, int limit, QueueMode mode, Location destination) {
        this.name = name;
        this.limit = limit;
        this.mode = mode;
        this.destination = destination;
    }

    /**
     * Makes the player join the queue, or leave it if they were already in this queue.
     * @param uuid UUID of the player
     */
    public void queueMember(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if(p == null) return;
        if(getMemberAmount() >= getLimit()) {
            p.sendActionBar(ComponentUtil.fromString(
                            Configuration.config.get("queuefull")
                    )
            );
            return;
        }
        if(getMembers().contains(uuid)) {
            p.sendActionBar(ComponentUtil.fromString(
                            Configuration.config.get("leavequeue"),
                            Placeholder.component("queuename", Component.text(getName()))
                    )
            );
            removeMember(uuid);
            updateSigns();
            return;
        }
        p.sendMessage(ComponentUtil.fromString(
                Configuration.config.get("joinqueue"),
                Placeholder.component("queuename", Component.text(getName()))
        ));
        members.add(uuid);
        updateSigns();
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
        updateSigns();
    }

    public int getMemberAmount() {
        return members.size();
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public void advance() {
        if (members.isEmpty()) return;

        Player p = null;
        while (p == null) {
            p = Bukkit.getPlayer(members.getFirst());
            members.removeFirst();
        }
        p.teleport(getDestination());
        p.sendActionBar(ComponentUtil.fromString(
                        Configuration.config.get("playerturn"),
                        Placeholder.component("queuename", Component.text(getName()))
                )
        );
        Sound sound = Sound.sound(Key.key(Configuration.config.get("attentionsound")), Sound.Source.MASTER, 1, 1);
        p.playSound(sound);
        updateSigns();
    }

    public void updateSigns() {
        for (QueueSign qs : getSigns()) {
            qs.update();
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("name", getName());
        serialized.put("mode", getMode().name());
        serialized.put("memberLimit", getLimit());
        serialized.put("destination", getDestination().serialize());
        serialized.put("interval", getInterval());
        Map<String, Object> signMap = new HashMap<>();
        int i = 0;
        for (QueueSign sign : getSigns()){
            signMap.put(String.valueOf(i), sign.serialize());
            i++;
        }
        serialized.put("signs", signMap);
        return serialized;
    }

    @SuppressWarnings("unchecked")
    public static Queue deserialize(Map<String, Object> map) {
        String name = map.get("name").toString();
        QueueMode mode = QueueMode.valueOf(map.get("mode").toString());
        int limit = Integer.parseInt(map.get("memberLimit").toString());
        Location destination = Location.deserialize(((MemorySection) map.get("destination")).getValues(false));
        Map<String, Object> signMap = ((MemorySection) map.get("signs")).getValues(false);
        int interval = (int) map.get("interval");

        Queue queue = new Queue(name, limit, mode, destination);

        queue.setInterval(interval);

        for (Object signSection : signMap.values()) {
            try {
                Sign sign = QueueSign.deserialize(((MemorySection)signSection).getValues(false));
                QueueSign qs = new QueueSign(name, sign);
                queue.addSign(qs);
            } catch (ClassCastException ex) {
                Queuer.getInstance().getLogger().warning("Sign for queue \"" + queue.getName() + "\" could not be loaded. Did it get removed?");
            }
        }

        return queue;
    }

    public String getName() { return this.name; }

    public int getLimit() { return this.limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public LinkedList<UUID> getMembers() { return this.members; }

    public Location getDestination() { return this.destination; }
    public void setDestination(Location l) { this.destination = l; }

    public QueueMode getMode() { return this.mode; }

    public void setMode(QueueMode mode) {
        this.mode = mode;
        if (mode == QueueMode.AUTO) {
            startTimer();
        } else if (mode == QueueMode.MANUAL) {
            stopTimer();
        }
    }

    public void addSign(QueueSign sign) {
        signs.add(sign);
        sign.update();
    }

    public void removeSign(QueueSign sign) {
        signs.remove(sign);
    }

    public List<QueueSign> getSigns() {
        return signs;
    }

    public void setInterval(int seconds) {
        interval = seconds;
        timer = seconds;
    }

    public int getInterval() {
        return interval;
    }

    public int getEstimatedWait(int queuePosition) {
        return timer + interval * queuePosition;
    }

    public void startTimer() {
        if(getMode() != QueueMode.AUTO) return;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Queuer.getInstance(), () -> {
            if (timer <= 0) {
                advance();
                timer = interval;
            }
            timer -= 1;
        }, 0, 20L);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
    }

}

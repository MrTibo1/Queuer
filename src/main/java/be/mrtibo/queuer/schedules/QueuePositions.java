package be.mrtibo.queuer.schedules;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.config.Configuration;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueManager;
import be.mrtibo.queuer.queues.QueueMode;
import be.mrtibo.queuer.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

public class QueuePositions {

    private int taskId = -1;

    public void start() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Queuer.getInstance(), runnable, 0, 20);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
    }

    private final Runnable runnable = () -> {
        QueueManager manager = Queuer.getManager();
        for (Queue queue : manager.getQueues()) {
            String queueName = queue.getName();
            for (UUID uuid : queue.getMembers()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;
                int position = queue.getMembers().indexOf(uuid);
                if(queue.getMode() == QueueMode.MANUAL) {
                    player.sendActionBar(ComponentUtil.fromString(
                            Configuration.config.get("queueposition"),
                            Placeholder.component("queuename", Component.text(queueName)),
                            Placeholder.component("queueposition", Component.text(position+1))
                    ));
                } else if (queue.getMode() == QueueMode.AUTO) {

                    Duration waitSeconds = Duration.ofSeconds(queue.getEstimatedWait(position));
                    String waitEstimate = "%02d:%02d".formatted(waitSeconds.toMinutesPart(), waitSeconds.toSecondsPart());

                    player.sendActionBar(ComponentUtil.fromString(
                            Configuration.config.get("queuepositionauto"),
                            Placeholder.component("queuename", Component.text(queueName)),
                            Placeholder.component("queueposition", Component.text(position+1)),
                            Placeholder.component("duration", Component.text(waitEstimate))
                    ));
                }
            }
        }
    };

}

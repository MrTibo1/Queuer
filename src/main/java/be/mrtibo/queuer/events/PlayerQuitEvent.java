package be.mrtibo.queuer.events;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {

    public PlayerQuitEvent() {
        Queuer.getInstance().getServer().getPluginManager().registerEvents(this, Queuer.getInstance());
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        Player p = event.getPlayer();
        QueueManager manager = Queuer.getManager();
        Queue queue = manager.getQueue(p.getUniqueId());
        if(queue != null) {
            queue.removeMember(p.getUniqueId());
        }
    }

}

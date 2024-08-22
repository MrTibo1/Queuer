package be.mrtibo.queuer.events;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.config.Configuration;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueManager;
import be.mrtibo.queuer.signs.QueueSign;
import be.mrtibo.queuer.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public class SignInteractEvent implements Listener {

    public SignInteractEvent() {
        Queuer.getInstance().getServer().getPluginManager().registerEvents(this, Queuer.getInstance());
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand().equals(EquipmentSlot.HAND)){
            if (Objects.requireNonNull(event.getClickedBlock()).getState() instanceof Sign sign) {
                QueueManager manager = Queuer.getManager();
                QueueSign qs = manager.getQueueSign(sign);
                if(qs != null) {
                    Queue queue = qs.getParentQueue();
                    Queue currentQueue = manager.getQueue(event.getPlayer().getUniqueId());
                    if (currentQueue != null && currentQueue != queue) {
                        event.getPlayer().sendMessage(
                                ComponentUtil.fromString(
                                        Configuration.config.get("alreadyqueued"),
                                        Placeholder.component("queuename", Component.text(currentQueue.getName()))
                                )
                        );
                        return;
                    }
                    queue.queueMember(event.getPlayer().getUniqueId());
                }
            }
        }
    }

}

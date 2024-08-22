package be.mrtibo.queuer.events;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.signs.QueueSign;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockBreakEvent implements Listener {

    public BlockBreakEvent() {
        Queuer.getInstance().getServer().getPluginManager().registerEvents(this, Queuer.getInstance());
    }

    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        if(event.getBlock().getState() instanceof Sign sign) {
            QueueSign qs = Queuer.getManager().getQueueSign(sign);
            if(qs != null) {
                if(!event.getPlayer().hasPermission("queuer.admin")){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>You don't have permission to break this sign."));
                    return;
                }
                qs.getParentQueue().removeSign(qs);
                Queuer.getManager().save();
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<green>QueueSign for queue <yellow>%s</yellow> removed from memory.".formatted(qs.getParentQueue().getName())));
            }
        }
    }

}

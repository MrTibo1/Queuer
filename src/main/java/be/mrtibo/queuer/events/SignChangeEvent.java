package be.mrtibo.queuer.events;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.signs.QueueSign;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SignChangeEvent implements Listener {

    public SignChangeEvent() {
        Queuer.getInstance().getServer().getPluginManager().registerEvents(this, Queuer.getInstance());
    }

    @EventHandler
    public void onSignChange(org.bukkit.event.block.SignChangeEvent e) {
        String line0 = PlainTextComponentSerializer.plainText().serialize(e.line(0));
        String line1 = PlainTextComponentSerializer.plainText().serialize(e.line(1));
        Sign sign = (Sign) e.getBlock().getState();
        if(line0 == null || line1 == null) {
            return;
        }
        if(!line0.equalsIgnoreCase("[queue]")){
            return;
        }
        if(!e.getPlayer().hasPermission("queuer.admin")) {
            return;
        }
        Queue queue = Queuer.getManager().getQueue(line1);
        if (queue == null) {
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>The entered queue is not registered"));
            e.getBlock().breakNaturally();
            return;
        }
        QueueSign qs = new QueueSign(queue.getName(), sign);
        queue.addSign(qs);
        Queuer.getManager().save();
        e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<green>New sign created for queue <yellow>%s".formatted(queue.getName())));
    }

}

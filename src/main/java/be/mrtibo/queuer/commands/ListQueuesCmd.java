package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class ListQueuesCmd {

    public void execute(CommandSender sender) {
        QueueManager manager = Queuer.getManager();
        Component message = MiniMessage.miniMessage().deserialize("<green>The following queues are registered:");
        for (Queue queue : manager.getQueues()) {
            message = message.append(MiniMessage.miniMessage().deserialize("<br><gray>-</gray> <yellow>" + queue.getName() + "</yellow>"));
        }
        sender.sendMessage(message);
    }

}

package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class RemoveQueueCmd {

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>You need to provide the name of the queue you want to remove"
                    )
            );
            return;
        }
        QueueManager manager = Queuer.getManager();
        Queue queue = manager.getQueue(args[1]);
        if (queue == null) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>A queue by this name doesn't exist"
                    )
            );
            return;
        }
        manager.removeQueue(queue);
        sender.sendMessage(MiniMessage.miniMessage().deserialize(
                "<green>Queue deleted"
        ));
    }

}

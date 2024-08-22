package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class SetLimitCmd {

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Usage: /queuer setlimit <queue> <limit>");
            return;
        }

        QueueManager manager = Queuer.getManager();
        Queue queue = manager.getQueue(args[1]);
        if (queue == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<red>A queue by this name does not exist"
            ));
            return;
        }
        int limit;
        try {
            limit = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<red>Invalid integer"
            ));
            return;
        }
        if (!(0 < limit && limit <= 1000)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<red>The limit must be in range 1-1000"
            ));
            return;
        }
        queue.setLimit(limit);
        manager.save();
        sender.sendMessage(MiniMessage.miniMessage().deserialize(
                "<green>Limit of queue <yellow>%s</yellow> set to <yellow>%s</yellow>".formatted(
                        queue.getName(),
                        limit
                )
        ));
    }

}

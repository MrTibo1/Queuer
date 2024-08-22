package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueMode;
import be.mrtibo.queuer.util.ComponentUtil;
import org.bukkit.command.CommandSender;

public class SetModeCmd {

    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage("Usage: /queuer setmode <queue> <mode>");
            return;
        }
        Queue queue = Queuer.getManager().getQueue(args[1]);
        if (queue==null) {
            sender.sendMessage(ComponentUtil.fromString("<red>This queue does not exist"));
            return;
        }
        QueueMode mode;
        try {
            mode = QueueMode.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException exception) {
            sender.sendMessage(ComponentUtil.fromString("<red>Available mode options are AUTO or MANUAL"));
            return;
        }

        queue.setMode(mode);
        sender.sendMessage(ComponentUtil.fromString("<green>Queue mode updated to %s".formatted(mode.name())));
        Queuer.getManager().save();

    }

}

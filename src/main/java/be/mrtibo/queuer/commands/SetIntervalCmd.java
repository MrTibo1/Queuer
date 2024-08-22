package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.util.ComponentUtil;
import org.bukkit.command.CommandSender;

public class SetIntervalCmd {

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Usage: /queuer setinterval <queue> <seconds>");
            return;
        }

        Queue queue = Queuer.getManager().getQueue(args[1]);
        if (queue == null) {
            sender.sendMessage(ComponentUtil.fromString("<red>This queue does not exist"));
            return;
        }

        int interval;
        try {
            interval = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ComponentUtil.fromString("<red>Invalid integer"));
            return;
        }
        queue.setInterval(interval);
        sender.sendMessage(ComponentUtil.fromString("<green>Queue interval updated"));
        Queuer.getManager().save();
    }

}

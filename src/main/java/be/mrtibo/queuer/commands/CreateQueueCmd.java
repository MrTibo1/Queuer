package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueManager;
import be.mrtibo.queuer.queues.QueueMode;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateQueueCmd {

    CommandSender sender;
    String[] args;

    public CreateQueueCmd(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public void execute() {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Only players can execute this command");
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You need to provide a name for this queue."));
            return;
        }
        QueueManager manager = Queuer.getManager();
        String name = args[1];
        Queue queue = new Queue(name, 25, QueueMode.MANUAL, p.getLocation());
        manager.registerQueue(queue);
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>New queue registered with the name <yellow>" + name + "</yellow>."));
        manager.save();
    }
}

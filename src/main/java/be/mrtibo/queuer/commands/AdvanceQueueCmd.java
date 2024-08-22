package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import be.mrtibo.queuer.queues.QueueManager;
import be.mrtibo.queuer.queues.QueueMode;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class AdvanceQueueCmd {

    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You need to provide the name of the queue you want to advance"));
            return;
        }
        QueueManager manager = Queuer.getManager();
        Queue queue = manager.getQueue(args[1]);
        if(queue == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>A queue by this name does not exist"));
            return;
        }
        if(queue.getMode() != QueueMode.MANUAL){
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>This queue is not in manual mode and can not be advanced through commands"));
            return;
        }
        if (queue.isEmpty()) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>The queue is empty"));
            return;
        }
        queue.advance();
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Queue advanced"));
    }

}

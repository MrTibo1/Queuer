package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class QueuerCommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0) {
            return false;
        }

        switch (args[0]) {
            case "create":
                new CreateQueueCmd(commandSender, args).execute();
                break;
            case "save":
                Queuer.getManager().save();
                commandSender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Current queues have been force saved."));
                break;
            case "list":
                new ListQueuesCmd().execute(commandSender);
                break;
            case "advance":
                new AdvanceQueueCmd().execute(commandSender, args);
                break;
            case "remove":
                new RemoveQueueCmd().execute(commandSender, args);
                break;
            case "setlimit":
                new SetLimitCmd().execute(commandSender, args);
                break;
            case "setmode":
                new SetModeCmd().execute(commandSender, args);
                break;
            case "reload":
                new ReloadCmd().execute(commandSender);
                break;
            case "setinterval":
                new SetIntervalCmd().execute(commandSender, args);
                break;
        }

        return true;
    }

}

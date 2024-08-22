package be.mrtibo.queuer.commands;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.util.ComponentUtil;
import org.bukkit.command.CommandSender;

public class ReloadCmd {

    public void execute(CommandSender sender) {
        Queuer.getInstance().reloadConfig();
        Queuer.getConfiguration().loadConfig();
        sender.sendMessage(ComponentUtil.fromString("<green>Configuration has been reloaded"));
    }

}

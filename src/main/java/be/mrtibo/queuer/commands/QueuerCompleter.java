package be.mrtibo.queuer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QueuerCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length == 1) {
            List<String> returns = new ArrayList<>();
            returns.add("list");
            returns.add("create");
            returns.add("save");
            returns.add("advance");
            returns.add("remove");
            returns.add("setlimit");
            returns.add("setmode");
            returns.add("reload");
            returns.add("setinterval");
            return returns;
        }

        return new ArrayList<>();
    }
}

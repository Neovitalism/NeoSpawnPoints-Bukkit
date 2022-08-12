package me.neovitalism.neospawnpoints.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NSPReloadTabCompleter implements TabCompleter {
    private static final List<String> arguments = new ArrayList<>();
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        arguments.clear();
        if(args.length == 1) {
            arguments.add("reload");
            return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}

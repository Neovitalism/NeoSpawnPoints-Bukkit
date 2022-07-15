package me.neovitalism.neospawnpoints.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetSpawnTabCompleter implements TabCompleter {
    private static final List<String> arguments = new ArrayList<>();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        arguments.clear();
        if(args.length == 1) {
            arguments.add("[SpawnName]");
            return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
        } else if(args.length == 2) {
            arguments.add("<Priority>");
            return StringUtil.copyPartialMatches(args[1], arguments, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}

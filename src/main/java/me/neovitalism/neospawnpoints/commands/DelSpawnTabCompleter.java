package me.neovitalism.neospawnpoints.commands;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DelSpawnTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) {
            List<String> arguments = NeoSpawnPoints.INSTANCE.getSpawnNames();
            return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}

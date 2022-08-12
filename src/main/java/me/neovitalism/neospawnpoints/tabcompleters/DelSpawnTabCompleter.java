package me.neovitalism.neospawnpoints.tabcompleters;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DelSpawnTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) {
            Set<String> arguments = NeoSpawnPoints.getSpawnPointManagers().keySet();
            return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}

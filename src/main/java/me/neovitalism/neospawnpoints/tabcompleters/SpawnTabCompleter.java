package me.neovitalism.neospawnpoints.tabcompleters;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SpawnTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(sender.hasPermission("neospawnpoints.spawn.other")) {
            if(args.length == 1) {
                List<String> players = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
                return StringUtil.copyPartialMatches(args[0], players, new ArrayList<>());
            }
            if(args.length == 2) {
                Set<String> arguments = NeoSpawnPoints.getSpawnPointManagers().keySet();
                return StringUtil.copyPartialMatches(args[1], arguments, new ArrayList<>());
            }
        }
        return Collections.emptyList();
    }
}

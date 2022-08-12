package me.neovitalism.neospawnpoints.commands;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.SpawnPointManager;
import me.neovitalism.neospawnpoints.tabcompleters.SpawnTabCompleter;
import me.neovitalism.neospawnpoints.utils.SpawnUtil;
import me.neovitalism.neospawnpoints.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class SpawnCommand implements CommandExecutor {
    public SpawnCommand() {
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("spawn")).setExecutor(this);
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("spawn")).setTabCompleter(new SpawnTabCompleter());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!NeoSpawnPoints.ACTIVE) {
            sender.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cError in config! All commands are disabled until fixed. Contact an admin."));
            NeoSpawnPoints.printErrors();
            return false;
        }
        if(sender.hasPermission("neospawnpoints.spawn.other")) {
            if(args.length == 0) {
                if(sender instanceof Player) {
                    SpawnUtil.sendToTheirSpawn((Player) sender);
                    return true;
                } else {
                    Utils.sendMessage(sender, "&cInvalid Syntax. Try /spawn [player] <spawnName>");
                    return false;
                }
            }
            if(args.length == 1) {
                Player toTeleport = Bukkit.getPlayer(args[0]);
                if(toTeleport != null) {
                    SpawnUtil.sendToTheirSpawn(sender, toTeleport, false);
                    return true;
                } else {
                    Utils.sendMessage(sender, "&cThe targetted player must be online!");
                    return false;
                }
            } else if(args.length == 2) {
                Player toTeleport = Bukkit.getPlayer(args[0]);
                if(toTeleport != null) {
                    SpawnPointManager spawn = NeoSpawnPoints.getSpawnPointManagers().getOrDefault(args[1], null);
                    if(spawn != null) {
                        SpawnUtil.sendToSpecificSpawn(sender, toTeleport, spawn);
                        return true;
                    } else {
                        Utils.sendMessage(sender, "&cInvalid spawn point. Try /spawn [player] <spawnName>");
                        return false;
                    }
                } else {
                    Utils.sendMessage(sender, "&cInvalid player, or the player is not online. Try /spawn [player] <spawnName>");
                    return false;
                }
            }
            Utils.sendMessage(sender, "&cInvalid Syntax. Try /spawn [player] <spawnName>");
            return false;
        }
        SpawnUtil.sendToTheirSpawn((Player) sender);
        return true;
    }
}

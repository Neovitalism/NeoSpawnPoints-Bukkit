package me.neovitalism.neospawnpoints.commands;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.SpawnPointManager;
import me.neovitalism.neospawnpoints.utils.BlankTabComplete;
import me.neovitalism.neospawnpoints.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnCommand implements CommandExecutor {
    public SpawnCommand() {
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("spawn")).setExecutor(this);
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("spawn")).setTabCompleter(new BlankTabComplete());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (NeoSpawnPoints.ACTIVE) {
                List<SpawnPointManager> possibleTeleports = new ArrayList<>();
                int highestPriority = 0;
                for(SpawnPointManager s : NeoSpawnPoints.INSTANCE.getSpawnPointManager()) {
                    if(player.hasPermission(s.getPermission())) {
                        if(s.getPriority() > highestPriority) {
                            highestPriority = s.getPriority();
                            possibleTeleports.clear();
                            possibleTeleports.add(s);
                        } else if(s.getPriority() == highestPriority) {
                            possibleTeleports.add(s);
                        }
                    }
                }
                if(possibleTeleports.size() == 0) {
                    if(!NeoSpawnPoints.NO_SPAWN_MESSAGE.equals("")) {
                        player.sendMessage(Utils.parseColour(NeoSpawnPoints.CONFIG_PREFIX + NeoSpawnPoints.NO_SPAWN_MESSAGE));
                    }
                    return false;
                } else if(possibleTeleports.size() == 1) {
                    Location toTeleport = possibleTeleports.get(0).getLocation();
                    if(!NeoSpawnPoints.TELEPORTED_MESSAGE.equals("")) {
                        player.sendMessage(Utils.parseColour(NeoSpawnPoints.CONFIG_PREFIX + Utils.replaceLocationKeys(NeoSpawnPoints.TELEPORTED_MESSAGE, toTeleport)));
                    }
                    player.teleport(toTeleport);
                    return true;
                } else {
                    Location toTeleport = Utils.randomLocation(possibleTeleports);
                    if(!NeoSpawnPoints.TELEPORTED_MESSAGE.equals("")) {
                        player.sendMessage(Utils.parseColour(NeoSpawnPoints.CONFIG_PREFIX + Utils.replaceLocationKeys(NeoSpawnPoints.TELEPORTED_MESSAGE, toTeleport)));
                    }
                    player.teleport(toTeleport);
                    return true;
                }
            } else {
                player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cError in config! All commands are disabled until fixed. Contact an admin."));
                NeoSpawnPoints.printErrors();
                return false;
            }
        } else {
            sender.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cThis command can only be executed by a player."));
            return false;
        }
    }
}

package me.neovitalism.neospawnpoints.utils;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.SpawnPointManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnUtil {
    public static String teleportedMessage;
    public static String noSpawnMessage;
    public static String noSpawnMessageAdmin;
    public static String forceSpawnMessageAdmin;
    public static String forceSpecificSpawnAdmin;
    public static String forceSpawnMessagePlayer;
    public static String forceSpecificSpawnPlayer;
    public static boolean showForcedSpawnMessagePlayer;

    public static void sendToSpecificSpawn(CommandSender sender, Player player, SpawnPointManager spawn) {
        Utils.sendMessage(sender, forceSpecificSpawnAdmin, spawn.getLocation(), "{player}:" + player.getName(), "{spawn}:" + spawn.getSpawnPointName());
        if(showForcedSpawnMessagePlayer) {
            Utils.sendMessage(player, forceSpecificSpawnPlayer, spawn.getLocation(), "{player}:" + player.getName(), "{spawn}:" + spawn.getSpawnPointName());
        }
        sendToSpawn(player, spawn.getLocation());
    }

    public static void sendToTheirSpawn(Player player) {
        sendToTheirSpawn(player, player, true);
    }

    public static void sendToTheirSpawn(CommandSender sender, Player toTeleport, boolean samePlayer) {
        Location spawn = determineSpawnPoint(toTeleport);
        if(spawn != null) {
            sendToSpawn(toTeleport, spawn);
            if(samePlayer) {
                Utils.sendMessage(toTeleport, teleportedMessage, spawn);
            } else {
                Utils.sendMessage(sender, forceSpawnMessageAdmin, spawn, "{player}:" + toTeleport.getName());
                if(showForcedSpawnMessagePlayer) {
                    Utils.sendMessage(toTeleport, forceSpawnMessagePlayer, spawn);
                }
            }
        } else {
            if(samePlayer) {
                Utils.sendMessage(toTeleport, noSpawnMessage);
            } else {
                Utils.sendMessage(sender, noSpawnMessageAdmin, null, "{player}:" + toTeleport.getName());
            }
        }
    }

    private static void sendToSpawn(Player player, Location location) {
        player.teleport(location);
    }

    public static Location determineSpawnPoint(Player player) {
        List<SpawnPointManager> possibleTeleports = new ArrayList<>();
        int highestPriority = 0;
        for(SpawnPointManager s : NeoSpawnPoints.getSpawnPointManagers().values()) {
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
            return null;
        } else if(possibleTeleports.size() == 1) {
            return possibleTeleports.get(0).getLocation();
        } else {
            return Utils.randomLocation(possibleTeleports);
        }
    }
}


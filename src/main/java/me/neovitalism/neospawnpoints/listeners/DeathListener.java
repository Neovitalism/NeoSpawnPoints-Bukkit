package me.neovitalism.neospawnpoints.listeners;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.utils.SpawnUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener implements Listener {
    public static boolean spawnNoRespawn;

    public DeathListener() {
        if(spawnNoRespawn) {
            Bukkit.getPluginManager().registerEvents(this, NeoSpawnPoints.INSTANCE);
        }
    }

    public void disableListener() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent event) {
        if(event.getPlayer().getBedSpawnLocation() == null) {
            Location location = SpawnUtil.determineSpawnPoint(event.getPlayer());
            if(location != null) {
                event.setRespawnLocation(location);
            }
        }
    }
}

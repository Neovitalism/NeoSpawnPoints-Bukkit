package me.neovitalism.neospawnpoints.utils;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class FirstJoinListener implements Listener {
    @EventHandler(priority= EventPriority.HIGHEST)
    public void onFirstJoin(PlayerSpawnLocationEvent event) {
        if(NeoSpawnPoints.SPAWN_ON_FIRST_JOIN) {
            Player player = event.getPlayer();
            if(!player.hasPlayedBefore()) {
                event.setSpawnLocation(NeoSpawnPoints.FIRST_JOIN_SPAWN);
            }
        }
    }
}

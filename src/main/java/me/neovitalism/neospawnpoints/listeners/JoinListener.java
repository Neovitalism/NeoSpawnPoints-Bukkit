package me.neovitalism.neospawnpoints.listeners;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.utils.SpawnUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Objects;

public class JoinListener implements Listener {
    @EventHandler(priority= EventPriority.HIGHEST)
    public void onFirstJoin(PlayerSpawnLocationEvent event) {
        if(NeoSpawnPoints.SPAWN_ON_FIRST_JOIN) {
            Player player = event.getPlayer();
            if(!player.hasPlayedBefore()) {
                event.setSpawnLocation(NeoSpawnPoints.FIRST_JOIN_SPAWN);
                return;
            }
        }
        if(NeoSpawnPoints.FORCE_SPAWN_ON_JOIN) {
            event.setSpawnLocation(Objects.requireNonNull(SpawnUtil.determineSpawnPoint(event.getPlayer())));
        }
    }
}

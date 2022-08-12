package me.neovitalism.neospawnpoints;

import me.neovitalism.neospawnpoints.utils.Utils;
import org.bukkit.Location;

public class SpawnPointManager {
    private final String spawnPointName;
    private final Location location;
    private final int priority;
    public SpawnPointManager(String spawnPointName, String location, int priority) {
        this.spawnPointName = spawnPointName;
        this.location = Utils.parseLocation(location);
        this.priority = priority;
    }

    public String getPermission() {
        return "neospawnpoints." + spawnPointName;
    }

    public boolean validateLocation() {
        return location != null;
    }

    public Location getLocation() {
        return location;
    }

    public int getPriority() {
        return priority;
    }

    public String getSpawnPointName() {
        return spawnPointName;
    }
}

package me.neovitalism.neospawnpoints;

import me.neovitalism.neospawnpoints.commands.DelSpawnCommand;
import me.neovitalism.neospawnpoints.commands.NSPReloadCommand;
import me.neovitalism.neospawnpoints.commands.SetSpawnCommand;
import me.neovitalism.neospawnpoints.commands.SpawnCommand;
import me.neovitalism.neospawnpoints.listeners.DeathListener;
import me.neovitalism.neospawnpoints.listeners.FirstJoinListener;
import me.neovitalism.neospawnpoints.utils.SpawnUtil;
import me.neovitalism.neospawnpoints.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeoSpawnPoints extends JavaPlugin {
    public static final String PREFIX = "&#696969[&#9632faN&#9831efe&#9a30e4o&#9b2ed9S&#9d2dcep&#9f2cc3a&#a12bb8w&#a229adn&#a428a2P&#a62797o&#a8268ci&#a92481n&#ab2376t&#ad226bs&#696969]&f ";
    public static NeoSpawnPoints INSTANCE;
    public static boolean ACTIVE;
    private static ConfigurationSection spawnPoints;
    private final List<String> errors = new ArrayList<>();
    private static final Map<String, SpawnPointManager> spawnPointManagers = new HashMap<>();
    public static String CONFIG_PREFIX;
    public static boolean SPAWN_ON_FIRST_JOIN = false;
    public static Location FIRST_JOIN_SPAWN;
    private final Listener listener = new FirstJoinListener();
    private static FileConfiguration config;
    private static DeathListener deathListener = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        configManager();
        new SetSpawnCommand();
        new DelSpawnCommand();
        new NSPReloadCommand();
        new SpawnCommand();
    }

    public void configManager() {
        spawnPointManagers.clear();
        errors.clear();
        ACTIVE = true;
        saveDefaultConfig();
        reloadConfig();
        config = this.getConfig();
        HandlerList.unregisterAll(listener);

        CONFIG_PREFIX = (config.contains("Prefix", true)) ? config.getString("Prefix") : "";
        if(CONFIG_PREFIX != null && CONFIG_PREFIX.equals("DEFAULT")) CONFIG_PREFIX = PREFIX;
        SpawnUtil.teleportedMessage = config.getString("Teleported-Message", null);
        SpawnUtil.noSpawnMessage = config.getString("No-Spawn-Message", null);
        SpawnUtil.noSpawnMessageAdmin = config.getString("No-Spawn-Message-Admin", null);
        SpawnUtil.forceSpawnMessageAdmin = config.getString("Force-Spawn-Message-Admin", null);
        SpawnUtil.forceSpecificSpawnAdmin = config.getString("Force-Specific-Spawn-Admin", null);
        SpawnUtil.forceSpawnMessagePlayer = config.getString("Force-Spawn-Message-Player", null);
        SpawnUtil.forceSpecificSpawnPlayer = config.getString("Force-Specific-Spawn-Player", null);
        SpawnUtil.showForcedSpawnMessagePlayer = config.getBoolean("Show-Forced-Spawn-Message-Player", false);
        DeathListener.spawnNoRespawn = config.getBoolean("Spawn-No-Respawn", false);

        spawnPoints = config.getConfigurationSection("SpawnPoints");
        if(spawnPoints != null) {
            for(String spawnKey : spawnPoints.getKeys(false)) {
                if (!spawnPoints.contains(spawnKey + ".location", true)) {
                    errors.add("&cMissing location for spawn point: " + spawnKey);
                }
                if (!spawnPoints.contains(spawnKey + ".priority", true)) {
                    errors.add("&cMissing priority for spawn point: " + spawnKey);
                }
                SpawnPointManager newSpawn = new SpawnPointManager(spawnKey, spawnPoints.getString(spawnKey + ".location"),
                        spawnPoints.getInt(spawnKey + ".priority"));
                if(!newSpawn.validateLocation()) {
                    errors.add("&cFailed to parse location for spawn point: &4\"&c" + spawnKey + "&4\"&c.");
                } else {
                    spawnPointManagers.put(spawnKey, newSpawn);
                }
            }
        } else {
            spawnPoints = config.createSection("SpawnPoints");
            saveAndReloadConfig();
        }
        firstJoinTest(config);
        if(errors.size() != 0) {
            ACTIVE = false;
            printErrors();
        }
        if(deathListener != null) deathListener.disableListener();
        deathListener = new DeathListener();
    }

    private static void saveAndReloadConfig() {
        INSTANCE.saveConfig();
        INSTANCE.reloadConfig();
        config = INSTANCE.getConfig();
    }

    public static void printErrors() {
        for(String error : INSTANCE.errors) {
            INSTANCE.getServer().getConsoleSender().sendMessage(Utils.parseColour(PREFIX + error));
        }
    }

    public static boolean createSpawnPoint(String spawnPointName, Location location, int priority) {
        boolean created;
        if(!config.isConfigurationSection("SpawnPoints")) {
            config.createSection("SpawnPoints");
        }
        if(!spawnPoints.contains(spawnPointName, true)) {
            spawnPoints.createSection(spawnPointName);
            created = true;
        } else {
            created = false;
        }
        spawnPoints.set(spawnPointName + ".location", Utils.locationToString(location));
        spawnPoints.set(spawnPointName + ".priority", priority);
        saveAndReloadConfig();
        INSTANCE.configManager();
        return created;
    }

    public static boolean deleteSpawnPoint(String spawnPointName) {
        if(spawnPoints.contains(spawnPointName, true)) {
            spawnPoints.set(spawnPointName, null);
            INSTANCE.saveConfig();
            INSTANCE.configManager();
            return true;
        }
        return false;
    }

    public void firstJoinTest(FileConfiguration config) {
        SPAWN_ON_FIRST_JOIN = config.getBoolean("Spawn-On-First-Join", false);
        if(SPAWN_ON_FIRST_JOIN) {
            String firstJoinSpawn = config.getString("First-Join-Spawn", "");
            if(firstJoinSpawn.equals("")) {
                errors.add("&cInvalid First-Join-Spawn. Either disable Spawn-On-First-Join or fix this.");
            } else {
                SpawnPointManager firstSpawn = spawnPointManagers.getOrDefault(firstJoinSpawn, null);
                if(firstSpawn != null) {
                    FIRST_JOIN_SPAWN = firstSpawn.getLocation();
                    if(FIRST_JOIN_SPAWN == null) {
                        errors.add("&cUnable to parse the location of the First-Join-Spawn.");
                    } else {
                        INSTANCE.getServer().getPluginManager().registerEvents(listener, INSTANCE);
                    }
                }
            }
        }
    }

    public boolean isErrors() {
        return !(errors.size() == 0);
    }

    public static Map<String, SpawnPointManager> getSpawnPointManagers() {
        return spawnPointManagers;
    }
}

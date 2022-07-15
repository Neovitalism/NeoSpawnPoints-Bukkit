package me.neovitalism.neospawnpoints;

import me.neovitalism.neospawnpoints.commands.DelSpawnCommand;
import me.neovitalism.neospawnpoints.commands.NSPReloadCommand;
import me.neovitalism.neospawnpoints.commands.SetSpawnCommand;
import me.neovitalism.neospawnpoints.commands.SpawnCommand;
import me.neovitalism.neospawnpoints.utils.FirstJoinListener;
import me.neovitalism.neospawnpoints.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class NeoSpawnPoints extends JavaPlugin {
    public static final String PREFIX = "&#696969[&#9632faN&#9831efe&#9a30e4o&#9b2ed9S&#9d2dcep&#9f2cc3a&#a12bb8w&#a229adn&#a428a2P&#a62797o&#a8268ci&#a92481n&#ab2376t&#ad226bs&#696969]&f ";
    public static NeoSpawnPoints INSTANCE;
    public static boolean ACTIVE;
    private static ConfigurationSection spawnPoints;
    private final List<SpawnPointManager> spawnPointManager = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();
    private final List<String> spawnNames = new ArrayList<>();
    public static String CONFIG_PREFIX;
    public static String TELEPORTED_MESSAGE;
    public static String NO_SPAWN_MESSAGE;
    public static boolean SPAWN_ON_FIRST_JOIN = false;
    public static Location FIRST_JOIN_SPAWN;
    private final Listener listener = new FirstJoinListener();
    private static FileConfiguration config;

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
        spawnPointManager.clear();
        errors.clear();
        spawnNames.clear();
        ACTIVE = true;
        saveDefaultConfig();
        reloadConfig();
        config = this.getConfig();
        HandlerList.unregisterAll(listener);
        CONFIG_PREFIX = (config.contains("Prefix", true)) ? config.getString("Prefix") : "";
        if(CONFIG_PREFIX != null && CONFIG_PREFIX.equals("DEFAULT")) CONFIG_PREFIX = PREFIX;
        TELEPORTED_MESSAGE = (config.contains("Teleported-Message", true)) ? config.getString("Teleported-Message") : "";
        NO_SPAWN_MESSAGE = (config.contains("No-Spawn-Message", true)) ? config.getString("No-Spawn-Message") : "";
        spawnPoints = config.getConfigurationSection("SpawnPoints");
        if(spawnPoints != null) {
            for(String spawnKey : spawnPoints.getKeys(false)) {
                if(!spawnPoints.contains(spawnKey + ".location", true)) {
                    errors.add("&cMissing location for spawn point: " + spawnKey);
                }
                if(!spawnPoints.contains(spawnKey + ".priority", true)) {
                    errors.add("&cMissing priority for spawn point: " + spawnKey);
                }
                spawnPointManager.add(new SpawnPointManager(spawnKey, spawnPoints.getString(spawnKey + ".location"), spawnPoints.getInt(spawnKey + ".priority")));
            }
        } else {
            spawnPoints = config.createSection("SpawnPoints");
            saveAndReloadConfig();
        }
        for(SpawnPointManager s : spawnPointManager) {
            spawnNames.add(s.getSpawnPointName());
            if(!s.validateLocation()) {
                errors.add("&cFailed to parse location for spawn point: &4\"&c" + s.getSpawnPointName() + "&4\"&c.");
            }
        }
        firstJoinTest(config);
        if(errors.size() != 0) {
            ACTIVE = false;
            printErrors();
        }
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

    public List<String> getSpawnNames() {
        return spawnNames;
    }

    public List<SpawnPointManager> getSpawnPointManager() {
        return spawnPointManager;
    }

    public void firstJoinTest(FileConfiguration config) {
        SPAWN_ON_FIRST_JOIN = config.getBoolean("Spawn-On-First-Join", false);
        if(SPAWN_ON_FIRST_JOIN) {
            String firstJoinSpawn = config.getString("First-Join-Spawn", "");
            if(firstJoinSpawn.equals("")) {
                errors.add("&cInvalid First-Join-Spawn. Either disable Spawn-On-First-Join or fix this.");
            }
            for(SpawnPointManager s : spawnPointManager) {
                if(s.getSpawnPointName().equals(firstJoinSpawn)) {
                    FIRST_JOIN_SPAWN = s.getLocation();
                    INSTANCE.getServer().getPluginManager().registerEvents(listener, INSTANCE);
                    break;
                }
            }
            if(FIRST_JOIN_SPAWN == null) {
                errors.add("&cUnable to parse the location of the First-Join-Spawn.");
            }
        }
    }

    public boolean isErrors() {
        return !(errors.size() == 0);
    }
}

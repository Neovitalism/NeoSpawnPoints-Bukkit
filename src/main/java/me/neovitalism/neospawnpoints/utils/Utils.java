package me.neovitalism.neospawnpoints.utils;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.SpawnPointManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]){6}");
    private static final String PLACEHOLDER = "\udcba\udcba\udcba\udcba\udcba";
    public static String parseColour(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            final ChatColor hexColor = ChatColor.of(matcher.group().substring(1));
            final String before = message.substring(0, matcher.start());
            final String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = HEX_PATTERN.matcher(message);
        }
        message = message.replace("\\\\n", PLACEHOLDER).replace("\\n", "\n").replace(PLACEHOLDER, "\\n");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Location parseLocation(String locationString) {
        double locX;
        double locY;
        double locZ;
        float yaw;
        float pitch;
        String worldUUID;
        String[] locationInstructions = locationString.split(",");
        try {
            locX = Double.parseDouble(locationInstructions[0].split(":")[1]);
            locY = Double.parseDouble(locationInstructions[1].split(":")[1]);
            locZ = Double.parseDouble(locationInstructions[2].split(":")[1]);
            yaw = Float.parseFloat(locationInstructions[3].split(":")[1]);
            pitch = Float.parseFloat(locationInstructions[4].split(":")[1]);
            worldUUID = locationInstructions[5].split(":")[1];
        } catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
        World world = NeoSpawnPoints.INSTANCE.getServer().getWorld(UUID.fromString(worldUUID));
        if(world == null) {
            return null;
        }
        return new Location(world, locX, locY, locZ, yaw, pitch);
    }

    public static String locationToString(Location location) {
        return "x:" + location.getX() + ",y:" + location.getY() + ",z:" + location.getZ() + ",yaw:" + location.getYaw() + ",pitch:" + location.getPitch() + ",world:" + location.getWorld().getUID();
    }

    public static String locationToReadableString(Location location) {
        return "&bX: &e" + String.format("%.2f", location.getX()) + "&a, &bY: &e" + String.format("%.2f", location.getY()) + "&a, &bZ: &e" +
                String.format("%.2f", location.getZ()) + "&a, &bYaw: &e" + String.format("%.2f", location.getYaw()) + "&a, &bPitch: &e" +
                String.format("%.2f", location.getPitch()) + "&a in &bWorld: &e" + location.getWorld().getName() + "&a.";
    }

    public static String replaceLocationKeys(String message, Location location) {
        message = message.replaceAll("\\{x}", String.format("%.2f", location.getX()));
        message = message.replaceAll("\\{y}", String.format("%.2f", location.getY()));
        message = message.replaceAll("\\{z}", String.format("%.2f", location.getZ()));
        message = message.replaceAll("\\{world}", location.getWorld().getName());
        return message;
    }

    public static Location randomLocation(List<SpawnPointManager> s) {
        double random = (Math.random() * s.size());
        return s.get((int) random).getLocation();
    }

    public static String argReplacer(String message, String... replacements) {
        for(String replacer : replacements) {
            String[] parser = replacer.split(":");
            message = message.replace(parser[0], parser[1]);
        }
        return message;
    }

    public static void sendMessage(CommandSender reciever, String message) {
        if(message != null && !message.isBlank() && !message.isEmpty()) {
            reciever.sendMessage(parseColour(NeoSpawnPoints.CONFIG_PREFIX + message));
        }
    }

    public static void sendMessage(CommandSender reciever, String message, Location location, String... replacements) {
        if(message != null && !message.isBlank() && !message.isEmpty()) {
            if(location != null) {
                message = replaceLocationKeys(message, location);
            }
            message = argReplacer(message, replacements);
            reciever.sendMessage(parseColour(NeoSpawnPoints.CONFIG_PREFIX + message));
        }
    }
}

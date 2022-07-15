package me.neovitalism.neospawnpoints.commands;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetSpawnCommand implements CommandExecutor {
    public SetSpawnCommand() {
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("setspawn")).setExecutor(this);
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("setspawn")).setTabCompleter(new SetSpawnTabCompleter());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(NeoSpawnPoints.ACTIVE) {
                if(args.length == 1) {
                    String created = NeoSpawnPoints.createSpawnPoint(args[0], player.getLocation(), 0) ? "&aCreated" : "&3Overwritten";
                    player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + created + " Spawn Point: &5\"&d" + args[0] + "&5\" &aat "
                            + Utils.locationToReadableString(player.getLocation()) + "&a with a &bPriority &aof &e0&a."));
                    return true;
                } else if(args.length == 2) {
                    try {
                        int priority = Integer.parseInt(args[1]);
                        String created = NeoSpawnPoints.createSpawnPoint(args[0], player.getLocation(), priority) ? "&aCreated" : "&3Overwritten";
                        player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + created + " Spawn Point: &5\"&d" + args[0] + "&5\" &aat "
                                + Utils.locationToReadableString(player.getLocation()) + "&a with a &bPriority &aof &e" + priority + "&a."));
                        return true;
                    } catch(NumberFormatException e) {
                        player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&c'" + args[1] + "' is not an integer. Priorities are only an integer."));
                        return false;
                    }
                } else {
                    player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cInvalid Syntax. Use '/setspawn [name] <priority>'."));
                    return false;
                }
            } else {
                player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cError in config! All commands are disabled until fixed. Error(s) will be printed in console."));
                NeoSpawnPoints.printErrors();
                return false;
            }
        } else {
            sender.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cThis command can only be executed by a player."));
            return false;
        }
    }
}

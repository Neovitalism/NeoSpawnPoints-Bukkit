package me.neovitalism.neospawnpoints.commands;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DelSpawnCommand implements CommandExecutor {
    public DelSpawnCommand() {
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("delspawn")).setExecutor(this);
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("delspawn")).setTabCompleter(new DelSpawnTabCompleter());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(NeoSpawnPoints.ACTIVE) {
                if(args.length == 1) {
                    boolean existed = NeoSpawnPoints.deleteSpawnPoint(args[0]);
                    if(existed) {
                        player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cSuccessfully deleted spawn point: &4\"&c" + args[0] + "&4\"&c."));
                        return true;
                    } else {
                        player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&c\"" + args[0] + "\" is not a valid spawn point."));
                        return false;
                    }
                } else {
                    player.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cInvalid Syntax. Use '/delspawn [name]'."));
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

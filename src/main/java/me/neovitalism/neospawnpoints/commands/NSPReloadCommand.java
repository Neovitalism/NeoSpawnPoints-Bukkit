package me.neovitalism.neospawnpoints.commands;

import me.neovitalism.neospawnpoints.NeoSpawnPoints;
import me.neovitalism.neospawnpoints.tabcompleters.NSPReloadTabCompleter;
import me.neovitalism.neospawnpoints.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NSPReloadCommand implements CommandExecutor {
    public NSPReloadCommand() {
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("nsp")).setExecutor(this);
        Objects.requireNonNull(NeoSpawnPoints.INSTANCE.getCommand("nsp")).setTabCompleter(new NSPReloadTabCompleter());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("reload")) {
                NeoSpawnPoints.INSTANCE.configManager();
                if(!NeoSpawnPoints.INSTANCE.isErrors()) {
                    sender.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&aReloaded config."));
                } else {
                    sender.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cError in config! All other commands are disabled until fixed. Error(s) will be printed in console."));
                }
                return true;
            }
        }
        sender.sendMessage(Utils.parseColour(NeoSpawnPoints.PREFIX + "&cInvalid Syntax. Use '/nsp reload'."));
        return false;
    }
}

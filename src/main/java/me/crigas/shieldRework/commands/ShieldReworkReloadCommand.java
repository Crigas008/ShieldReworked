package me.crigas.shieldRework.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Subcommand;
import me.crigas.shieldRework.ShieldRework;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@Command("shieldrework")
public class ShieldReworkReloadCommand {
    @Subcommand("reload")
    public static void reload(CommandSender sender) {
        ShieldRework.getConfigFile().load(true);
        sender.sendMessage("§aShieldRework config reloaded!");
    }
}
package me.crigas.shieldRework;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import me.crigas.shieldRework.commands.ShieldReworkReloadCommand;
import me.crigas.shieldRework.listeners.OnKnockback;
import me.crigas.shieldRework.listeners.OnPlayerDamage;
import me.crigas.shieldRework.listeners.ShieldHoldListener;
import me.crigas.shieldRework.listeners.ShieldSpeedBoostListener;
import me.crigas.shieldRework.utils.Config;
import me.playgamesgo.plugin.annotation.plugin.ApiVersion;
import me.playgamesgo.plugin.annotation.plugin.Description;
import me.playgamesgo.plugin.annotation.plugin.Plugin;
import me.playgamesgo.plugin.annotation.plugin.author.Author;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@ApiVersion(ApiVersion.Target.v1_21)
@Plugin( name = "ShieldRework", version = "${version}")
@Author("Crigas")
@Description("A rework of the shield system.")
public final class ShieldRework extends JavaPlugin {

    @Getter private static Config configFile;
    @Getter private static ShieldRework instance;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        instance = this;
        configFile = ConfigManager.create(Config.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(this.getDataFolder(), "config.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        getServer().getPluginManager().registerEvents(new OnKnockback(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDamage(), this);
        getServer().getPluginManager().registerEvents(new ShieldHoldListener(), this);
        getServer().getPluginManager().registerEvents(new ShieldSpeedBoostListener(), this);


        CommandAPI.registerCommand(ShieldReworkReloadCommand.class);

        CommandAPI.onEnable();



    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}

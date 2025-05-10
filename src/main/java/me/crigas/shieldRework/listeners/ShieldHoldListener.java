package me.crigas.shieldRework.listeners;

import me.crigas.shieldRework.ShieldRework;
import me.crigas.shieldRework.utils.ShieldUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldHoldListener implements Listener {
    private static class ShieldState {
        long holdStart;
        long pausedAt;
        long elapsed;
    }

    private final Map<UUID, ShieldState> shieldStates = new HashMap<>();

    @EventHandler
    public void onShieldUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        var config = ShieldRework.getConfigFile();

        int maxTime = config.getMaxShieldHoldTime();
        int cooldownTime = config.getCooldownTime();
        long now = System.currentTimeMillis();

        ShieldState state = shieldStates.get(uuid);

        if (!ShieldUtils.isHoldingShield(player)) {
            shieldStates.remove(uuid);
            return;
        }

        if (player.isBlocking()) {
            if (state == null) {
                state = new ShieldState();
                state.holdStart = now;
                shieldStates.put(uuid, state);
                if (config.isDebug()) player.sendMessage("§7[Debug] Started holding shield.");
            } else if (state.pausedAt > 0) {
                long pauseDuration = now - state.pausedAt;
                if (pauseDuration > (cooldownTime * 1000L) / 2) {
                    state.holdStart = now;
                    state.elapsed = 0;
                    if (config.isDebug()) player.sendMessage("§7[Debug] Shield pause too long, timer reset.");
                } else {
                    if (config.isDebug()) player.sendMessage("§7[Debug] Resumed shield hold after short pause.");
                }
                state.pausedAt = 0;
            }
            long totalHeld = state.elapsed + (now - state.holdStart);
            int secondsHeld = (int) (totalHeld / 1000);
            if (config.isShowShieldTimer() || config.isDebug()) {
                player.sendActionBar("§bShield held: " + secondsHeld + "s / " + maxTime + "s");
            }
            if (totalHeld / 1000 >= maxTime) {
                player.setCooldown(Material.SHIELD, 20 * cooldownTime);
                player.sendMessage("§cYou can't hold your shield any longer! Cooldown applied.");
                shieldStates.remove(uuid);

                if (config.isSpeedBoostParticles()) {
                    Particle particle = Particle.valueOf(config.getSpeedBoostParticle());
                    player.getWorld().spawnParticle(
                            particle,
                            player.getLocation().add(0, 1, 0),
                            config.getSpeedBoostParticleCount(),
                            0.5, 0.5, 0.5, 0.01
                    );
                }
                if (config.isDebug()) player.sendMessage("§7[Debug] Shield cooldown applied for " + cooldownTime + "s.");
            }
        } else {
            if (state != null && state.pausedAt == 0) {
                state.elapsed += now - state.holdStart;
                state.pausedAt = now;
                if (config.isDebug()) player.sendMessage("§7[Debug] Shield hold paused.");
            }
            player.setCooldown(Material.SHIELD, 0);
            if (config.isSpeedBoostParticles()) {
                Particle particle = Particle.valueOf(config.getSpeedBoostParticle());
                player.getWorld().spawnParticle(
                        particle,
                        player.getLocation().add(0, 1, 0),
                        config.getSpeedBoostParticleCount(),
                        0.5, 0.5, 0.5, 0.01
                );
            }
            if (config.isDebug()) player.sendMessage("§7[Debug] Shield cooldown reset.");
        }
    }
}
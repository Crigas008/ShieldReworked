package me.crigas.shieldRework.listeners;

import me.crigas.shieldRework.ShieldRework;
import me.crigas.shieldRework.utils.ShieldUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldHoldListener implements Listener {

    private final Map<UUID, ShieldSession> sessions = new HashMap<>();
    private final Map<UUID, Long> speedBoostCooldowns = new HashMap<>();

    private static class ShieldSession {
        int heldTime = 0;
        BukkitRunnable timerTask;
        boolean active = false;
    }

    @EventHandler
    public void onShieldInteract(PlayerInteractEvent event) {
        if (event.getAction().isLeftClick()) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.SHIELD) return;

        UUID uuid = player.getUniqueId();
        var config = ShieldRework.getConfigFile();
        int maxTime = config.getMaxShieldHoldTime();

        if (sessions.containsKey(uuid)) return;

        ShieldSession session = new ShieldSession();
        session.active = true;
        sessions.put(uuid, session);

        session.timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isBlocking() || !ShieldUtils.isHoldingShield(player)) {
                    endSession(player, session, false);
                    return;
                }
                session.heldTime++;
                if (config.isShowShieldTimer()) {
                    String msg = config.getShieldHoldTimerMsg()
                            .replace("{held}", String.valueOf(session.heldTime))
                            .replace("{max}", String.valueOf(maxTime));
                    player.sendActionBar(config.getMessagePrefix() + msg);
                }
                if (session.heldTime >= maxTime) {
                    applyCooldown(player, config);
                    endSession(player, session, true);
                }
            }
        };
        session.timerTask.runTaskTimer(ShieldRework.getInstance(), 0L, 20L);
    }

    private void endSession(Player player, ShieldSession session, boolean reachedMax) {
        if (session.timerTask != null) session.timerTask.cancel();
        sessions.remove(player.getUniqueId());

        if (!reachedMax) {
            applySpeedBoost(player);
        }
    }

    private void applyCooldown(Player player, me.crigas.shieldRework.utils.Config config) {
        player.setCooldown(Material.SHIELD, config.getCooldownTime() * 20);
        player.sendMessage(config.getMessagePrefix() + config.getShieldHoldCooldownMsg());
        try {
            player.getWorld().spawnParticle(
                    Particle.valueOf(config.getShieldCooldownParticle()),
                    player.getLocation().add(0, 1, 0),
                    config.getShieldCooldownParticleCount()
            );
        } catch (Exception ignored) {}
        try {
            player.playSound(
                    player.getLocation(),
                    Sound.valueOf(config.getShieldCooldownSound()),
                    SoundCategory.PLAYERS, 1F, 1F
            );
        } catch (Exception ignored) {}
    }

    private void applySpeedBoost(Player player) {
        var config = ShieldRework.getConfigFile();
        if (!config.isSpeedBoostEnabled()) return;

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long last = speedBoostCooldowns.getOrDefault(uuid, 0L);
        if (now - last < config.getSpeedBoostCooldown() * 1000L) return;

        int amplifier = (int) Math.round(config.getSpeedBoostAmount() * 10) - 1;
        if (amplifier < 0) amplifier = 0;
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                config.getSpeedBoostDuration() * 20,
                amplifier
        ));
        player.sendMessage(config.getMessagePrefix() + config.getSpeedBoostActivatedMsg());
        if (config.isSpeedBoostParticles()) {
            try {
                player.getWorld().spawnParticle(
                        Particle.valueOf(config.getSpeedBoostParticle()),
                        player.getLocation().add(0, 1, 0),
                        config.getSpeedBoostParticleCount(),
                        config.getSpeedBoostParticleOffsetX(),
                        config.getSpeedBoostParticleOffsetY(),
                        config.getSpeedBoostParticleOffsetZ(),
                        config.getSpeedBoostParticleExtra()
                );
            } catch (Exception ignored) {}
        }
        speedBoostCooldowns.put(uuid, now);
    }
}
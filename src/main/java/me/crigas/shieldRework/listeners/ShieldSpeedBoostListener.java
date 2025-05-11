package me.crigas.shieldRework.listeners;

import me.crigas.shieldRework.ShieldRework;
import me.crigas.shieldRework.utils.ShieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldSpeedBoostListener implements Listener {
    private final Map<UUID, Long> lastBoost = new HashMap<>();
    private final Map<UUID, AttributeModifier> activeModifiers = new HashMap<>();

    @EventHandler
    public void onShieldBlock(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!ShieldUtils.isHoldingShield(player) || !player.isBlocking()) return;

        var config = ShieldRework.getConfigFile();
        if (!config.isSpeedBoostEnabled()) return;

        int duration = config.getSpeedBoostDuration();
        double amount = config.getSpeedBoostAmount();
        int cooldown = config.getSpeedBoostCooldown();

        long now = System.currentTimeMillis();
        long last = lastBoost.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < cooldown * 1000L) return;

        AttributeInstance attr = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (attr == null) return;

        if (activeModifiers.containsKey(player.getUniqueId())) {
            attr.removeModifier(activeModifiers.get(player.getUniqueId()));
            activeModifiers.remove(player.getUniqueId());
        }

        AttributeModifier modifier = new AttributeModifier(
                new NamespacedKey(ShieldRework.getInstance(), "shield_speed_boost"),
                amount,
                AttributeModifier.Operation.ADD_SCALAR,
                EquipmentSlotGroup.ANY
        );
        attr.addModifier(modifier);
        activeModifiers.put(player.getUniqueId(), modifier);

        lastBoost.put(player.getUniqueId(), now);

        if (config.isSpeedBoostParticles()) {
            Particle particle = Particle.valueOf(config.getSpeedBoostParticle());
            player.getWorld().spawnParticle(
                    particle,
                    player.getLocation().add(0, 1, 0),
                    config.getSpeedBoostParticleCount(),
                    0.5, 0.5, 0.5, 0.01
            );
        }

        player.sendMessage("§aSpeed boost activated after blocking!");

        Bukkit.getScheduler().runTaskLater(ShieldRework.getInstance(), () -> {
            AttributeInstance attr2 = player.getAttribute(Attribute.MOVEMENT_SPEED);
            if (attr2 != null && activeModifiers.containsKey(player.getUniqueId())) {
                attr2.removeModifier(activeModifiers.get(player.getUniqueId()));
                activeModifiers.remove(player.getUniqueId());
                player.sendMessage("§eSpeed boost ended.");
            }
        }, duration * 20L);
    }
}
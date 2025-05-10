package me.crigas.shieldRework.listeners;

import io.papermc.paper.event.entity.EntityKnockbackEvent;
import me.crigas.shieldRework.ShieldRework;
import me.crigas.shieldRework.utils.ShieldUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnKnockback implements Listener {
    @EventHandler
    public void onKnockback(EntityKnockbackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ShieldUtils.isHoldingShield(player)) {
                double multiplier = ShieldRework.getConfigFile().getKnockbackMultiplier();
                event.setKnockback(event.getKnockback().multiply(multiplier));            }
        }
    }
}

package me.crigas.shieldRework.listeners;

import me.crigas.shieldRework.utils.ShieldUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnPlayerDamage implements Listener {

    @EventHandler
    public static void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (ShieldUtils.isHoldingShield(player)) {

            }

        }
    }

}

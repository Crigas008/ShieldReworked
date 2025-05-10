package me.crigas.shieldRework.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShieldUtils {

    public static boolean isHoldingShield(Player player) {
        return player.getInventory().getItemInMainHand().getType() == Material.SHIELD
                || player.getInventory().getItemInOffHand().getType() == Material.SHIELD;
    }


}

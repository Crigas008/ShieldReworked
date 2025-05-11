package me.crigas.shieldRework.listeners;

import me.crigas.shieldRework.ShieldRework;
import me.crigas.shieldRework.utils.ShieldUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OnPlayerDamage implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        var config = ShieldRework.getConfigFile();

        // Check both hands for shield
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (mainHand.getType() == Material.SHIELD || offHand.getType() == Material.SHIELD) {
            ItemStack shield = mainHand.getType() == Material.SHIELD ? mainHand : offHand;
            short newDur = (short) (shield.getDurability() + config.getShieldDurabilityReduction());
            shield.setDurability(newDur);

            if (config.isEnableBlockingEffects()) {
                PotionEffectType effectType = PotionEffectType.getByName(config.getBlockingEffectType());
                if (effectType != null) {
                    player.addPotionEffect(new PotionEffect(
                            effectType,
                            config.getBlockingEffectDuration(),
                            config.getBlockingEffectAmplifier()
                    ));
                }
            }
        }
    }
}
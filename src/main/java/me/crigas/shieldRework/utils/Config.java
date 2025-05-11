package me.crigas.shieldRework.utils;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Config extends OkaeriConfig {
    private int maxShieldHoldTime = 5;
    private int cooldownTime = 5;

    private boolean speedBoostEnabled = true;
    private double knockbackMultiplier = 0.5;
    @Comment("Attribute boost (e.g., 0.1 = +10% speed)")
    private double speedBoostAmount = 0.1;
    private int speedBoostDuration = 5;
    private int speedBoostCooldown = 5;

    private boolean speedBoostParticles = true;
    private String speedBoostParticle = "CLOUD";
    private int speedBoostParticleCount = 20;
    private double SpeedBoostParticleOffsetX = 0.5;
    private double SpeedBoostParticleOffsetY = 0.5;
    private double SpeedBoostParticleOffsetZ = 0.5;
    private double SpeedBoostParticleExtra = 0.1;

    private boolean showShieldTimer = true;

    private int shieldDurabilityReduction = 1;
    private boolean enableBlockingEffects = true;
    private String blockingEffectType = "REGENERATION";
    private int blockingEffectDuration = 100;
    private int blockingEffectAmplifier = 1;

    private String shieldCooldownSound = "BLOCK_ANVIL_LAND";
    private String shieldCooldownParticle = "SMOKE_NORMAL";
    private int shieldCooldownParticleCount = 20;

    @Comment("Messages")
    private String shieldHoldCooldownMsg = "§cYou can't hold your shield any longer! Cooldown applied.";
    private String shieldHoldTimerMsg = "§bShield held: {held}s / {max}s";
    private String speedBoostActivatedMsg = "§aSpeed boost activated after blocking!";
    private String messagePrefix = "§7[ShieldRework] ";
}
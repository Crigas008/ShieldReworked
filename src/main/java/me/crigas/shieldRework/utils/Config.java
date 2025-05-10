package me.crigas.shieldRework.utils;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Data;

@Data
public class Config extends OkaeriConfig {
    private boolean debug = false;
    private double knockbackMultiplier = 0.3;
    private int maxShieldHoldTime = 5;
    private int cooldownTime = 5;

    private boolean speedBoostEnabled = true;
    private double speedBoostAmount = 0.1; // Attribute boost (e.g., 0.1 = +10% speed)
    private int speedBoostDuration = 5; // seconds
    private int speedBoostCooldown = 5; // seconds

    private boolean speedBoostParticles = true;
    private String speedBoostParticle = "CLOUD";
    private int speedBoostParticleCount = 20;
    private boolean showShieldTimer = true;
}
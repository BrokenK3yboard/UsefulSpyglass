package com.brokenkeyboard.usefulspyglass.platform;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.Enchantment;

public interface IPlatformHelper {
    boolean hasSpyglassEnchant(Player player, ResourceKey<Enchantment> enchant);
    boolean hasPrecision(Player player);
    boolean testPrecisionCompat(Player player);
    void useSpyglassEnch();
    void setPrecisionBonus(Projectile projectile);
    double getPrecisionBonus(Projectile projectile);
}
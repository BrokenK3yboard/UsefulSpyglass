package com.brokenkeyboard.usefulspyglass.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public interface IPlatformHelper {
    EnchantmentCategory getSpyglassEnchCategory();
    boolean hasSpyglassEnchant(Player player, Enchantment enchantment);
    boolean hasPrecision(Player player);
    boolean testPrecisionCompat(Player player);
    void useSpyglassEnch();
}
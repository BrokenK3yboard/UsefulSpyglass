package com.brokenkeyboard.usefulspyglass.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public interface IPlatformHelper {
    EnchantmentCategory getSpyglassEnchCategory();
    boolean checkPrecisionSpyglass(Player player);
}
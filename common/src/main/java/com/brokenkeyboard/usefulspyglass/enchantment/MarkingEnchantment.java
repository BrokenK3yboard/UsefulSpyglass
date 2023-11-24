package com.brokenkeyboard.usefulspyglass.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MarkingEnchantment extends Enchantment {
    public MarkingEnchantment(EnchantmentCategory category, EquipmentSlot... slots) {
        super(Rarity.COMMON, category, slots);
    }

    public int getMinCost(int enchantmentLevel) {
        return 1 + (10 * (enchantmentLevel - 1));
    }

    public int getMaxCost(int enchantmentLevel) {
        return 50;
    }

    public int getMaxLevel() {
        return 3;
    }
}
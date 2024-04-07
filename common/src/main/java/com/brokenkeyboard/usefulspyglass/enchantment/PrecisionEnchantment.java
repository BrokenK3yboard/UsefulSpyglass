package com.brokenkeyboard.usefulspyglass.enchantment;

import com.brokenkeyboard.usefulspyglass.ModRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class PrecisionEnchantment extends Enchantment {

    public PrecisionEnchantment(EnchantmentCategory category, EquipmentSlot... slots) {
        super(Rarity.COMMON, category, slots);
    }

    public int getMinCost(int enchantmentLevel) {
        return 10;
    }

    public int getMaxCost(int enchantmentLevel) {
        return 20;
    }

    public boolean checkCompatibility(@NotNull Enchantment ench) {
        return super.checkCompatibility(ench) && ench != ModRegistry.MARKING;
    }
}
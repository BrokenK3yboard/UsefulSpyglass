package com.brokenkeyboard.usefulspyglass.enchantment;

import com.brokenkeyboard.usefulspyglass.ModRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class SpotterEnchantment extends Enchantment {

    public SpotterEnchantment(EnchantmentCategory category, EquipmentSlot... slots) {
        super(Rarity.COMMON, category, slots);
    }

    public int getMinCost(int enchantmentLevel) {
        return 20;
    }

    public int getMaxCost(int enchantmentLevel) {
        return 30;
    }

    public boolean checkCompatibility(@NotNull Enchantment ench) {
        return super.checkCompatibility(ench) && ench != ModRegistry.MARKING;
    }
}

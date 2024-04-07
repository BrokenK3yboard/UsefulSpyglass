package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.enchantment.MarkingEnchantment;
import com.brokenkeyboard.usefulspyglass.enchantment.PrecisionEnchantment;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModRegistry {

    public static final Map<ResourceLocation, Enchantment> ENCHANTMENTS = new HashMap<>();
    public static final EnchantmentCategory SPYGLASS = Services.PLATFORM.getSpyglassEnchCategory();

    public static final Enchantment MARKING = addEnchantment(new ResourceLocation(Constants.MOD_ID, "marking"), new MarkingEnchantment(SPYGLASS, EquipmentSlot.MAINHAND));
    public static final Enchantment PRECISION = addEnchantment(new ResourceLocation(Constants.MOD_ID, "precision"), new PrecisionEnchantment(SPYGLASS, EquipmentSlot.MAINHAND));

    public static Enchantment addEnchantment(ResourceLocation location, Enchantment enchantment) {
        ENCHANTMENTS.put(location, enchantment);
        return enchantment;
    }

    public static void registerEnchantment(BiConsumer<ResourceLocation, Enchantment> consumer) {
        for (Map.Entry<ResourceLocation, Enchantment> entry : ENCHANTMENTS.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }
}
package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.enchantment.MarkingEnchantment;
import com.brokenkeyboard.usefulspyglass.enchantment.PrecisionEnchantment;
import com.brokenkeyboard.usefulspyglass.enchantment.SpotterEnchantment;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModRegistry {

    public static final String MOD_ID = "usefulspyglass";
    public static final String MOD_NAME = "Useful Spyglass";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final Map<ResourceLocation, Enchantment> ENCHANTMENTS = new HashMap<>();
    public static final EnchantmentCategory SPYGLASS = Services.PLATFORM.getSpyglassEnchCategory();

    public static final Enchantment MARKING = addEnchantment(new ResourceLocation(ModRegistry.MOD_ID, "marking"), new MarkingEnchantment(SPYGLASS, EquipmentSlot.MAINHAND));
    public static final Enchantment PRECISION = addEnchantment(new ResourceLocation(ModRegistry.MOD_ID, "precision"), new PrecisionEnchantment(SPYGLASS, EquipmentSlot.MAINHAND));
    public static final Enchantment SPOTTER = addEnchantment(new ResourceLocation(ModRegistry.MOD_ID, "spotter"), new SpotterEnchantment(SPYGLASS, EquipmentSlot.MAINHAND));

    public static final EntityType<? extends SpotterEye> SPOTTER_EYE = EntityType.Builder.<SpotterEye>of(SpotterEye::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(4).build("spotter_eye");

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
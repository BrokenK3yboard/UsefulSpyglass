package com.brokenkeyboard.usefulspyglass;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class ModRegistry {

    public static final String MOD_ID = "usefulspyglass";
    public static final String MOD_NAME = "Useful Spyglass";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final TagKey<Item> SPYGLASS_ENCHANTABLE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "enchantable/spyglass"));
    public static final TagKey<Enchantment> SPYGLASS_EXCLUSIVE = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "exclusive_set/spyglass"));
    public static final ResourceKey<Enchantment> MARKING = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "marking"));
    public static final ResourceKey<Enchantment> PRECISION = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "precision"));
    public static final ResourceKey<Enchantment> SPOTTER = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "spotter"));

    public static final Predicate<ItemStack> IS_SPYGLASS = stack -> stack.is(Items.SPYGLASS);

    public static final EntityType<? extends SpotterEye> SPOTTER_EYE = EntityType.Builder.<SpotterEye>of(SpotterEye::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(4).build("spotter_eye");
}
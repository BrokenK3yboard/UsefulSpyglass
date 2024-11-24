package com.brokenkeyboard.usefulspyglass.datagen;

import com.brokenkeyboard.usefulspyglass.ModRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class Datagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        final FabricDataGenerator.Pack pack = generator.createPack();
        BlockProvider blockTags = pack.addProvider(BlockProvider::new);
        pack.addProvider(DynamicProvider::new);
        pack.addProvider((output, registriesFuture) -> new ItemProvider(output, registriesFuture, blockTags.contentsGetter()));
        pack.addProvider(EnchantmentProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, Datagen::enchantmentBC);
    }

    protected static void enchantmentBC(BootstrapContext<Enchantment> context) {
        HolderSet.Named<Item> itemHolder = context.lookup(Registries.ITEM).getOrThrow(ModRegistry.SPYGLASS_ENCHANTABLE);
        HolderGetter<Enchantment> enchHolder = context.lookup(Registries.ENCHANTMENT);

        context.register(ModRegistry.MARKING, Enchantment.enchantment(Enchantment.definition(itemHolder, 10, 1, Enchantment.constantCost(1), Enchantment.constantCost(10), 1, EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchHolder.getOrThrow(ModRegistry.SPYGLASS_EXCLUSIVE)).build(ModRegistry.MARKING.location()));

        context.register(ModRegistry.PRECISION, Enchantment.enchantment(Enchantment.definition(itemHolder, 10, 1, Enchantment.constantCost(11), Enchantment.constantCost(20), 1, EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchHolder.getOrThrow(ModRegistry.SPYGLASS_EXCLUSIVE)).build(ModRegistry.PRECISION.location()));

        context.register(ModRegistry.SPOTTER, Enchantment.enchantment(Enchantment.definition(itemHolder, 10, 1, Enchantment.constantCost(21), Enchantment.constantCost(50), 1, EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchHolder.getOrThrow(ModRegistry.SPYGLASS_EXCLUSIVE)).build(ModRegistry.SPOTTER.location()));
    }
}

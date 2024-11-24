package com.brokenkeyboard.usefulspyglass.datagen;

import com.brokenkeyboard.usefulspyglass.ModRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

public class DynamicProvider extends FabricDynamicRegistryProvider {

    public DynamicProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        HolderSet.Named<Item> itemHolder = registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModRegistry.SPYGLASS_ENCHANTABLE);
        HolderGetter<Enchantment> enchHolder = registries.lookupOrThrow(Registries.ENCHANTMENT);

        entries.add(ModRegistry.MARKING, Enchantment.enchantment(Enchantment.definition(itemHolder, 10, 1, Enchantment.constantCost(1), Enchantment.constantCost(10), 1, EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchHolder.getOrThrow(ModRegistry.SPYGLASS_EXCLUSIVE)).build(ModRegistry.MARKING.location()));

        entries.add(ModRegistry.PRECISION, Enchantment.enchantment(Enchantment.definition(itemHolder, 10, 1,Enchantment.constantCost(11), Enchantment.constantCost(20), 1, EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchHolder.getOrThrow(ModRegistry.SPYGLASS_EXCLUSIVE)).build(ModRegistry.PRECISION.location()));

        entries.add(ModRegistry.SPOTTER, Enchantment.enchantment(Enchantment.definition(itemHolder, 10, 1, Enchantment.constantCost(21), Enchantment.constantCost(50), 1, EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchHolder.getOrThrow(ModRegistry.SPYGLASS_EXCLUSIVE)).build(ModRegistry.SPOTTER.location()));
    }

    @Override
    public String getName() {
        return "DynamicData";
    }
}

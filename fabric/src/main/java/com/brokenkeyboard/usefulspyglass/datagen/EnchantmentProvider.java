package com.brokenkeyboard.usefulspyglass.datagen;

import com.brokenkeyboard.usefulspyglass.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

public class EnchantmentProvider extends EnchantmentTagsProvider {

    public EnchantmentProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModRegistry.SPYGLASS_EXCLUSIVE).add(ModRegistry.MARKING).add(ModRegistry.PRECISION).add(ModRegistry.SPOTTER);
        tag(EnchantmentTags.NON_TREASURE).add(ModRegistry.MARKING).add(ModRegistry.PRECISION).add(ModRegistry.SPOTTER);
    }
}

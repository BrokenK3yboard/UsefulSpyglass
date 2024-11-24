package com.brokenkeyboard.usefulspyglass.datagen;

import com.brokenkeyboard.usefulspyglass.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ItemProvider extends ItemTagsProvider {

    public ItemProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModRegistry.SPYGLASS_ENCHANTABLE).add(Items.SPYGLASS);
    }
}

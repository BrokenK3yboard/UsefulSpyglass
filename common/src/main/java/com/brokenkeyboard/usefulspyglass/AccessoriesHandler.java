package com.brokenkeyboard.usefulspyglass;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.function.Predicate;

public class AccessoriesHandler {

    public static boolean checkAccessories(Player player, ResourceKey<Enchantment> enchantment) {
        AccessoriesCapability capability = AccessoriesCapability.get(player);
        if (capability == null) return false;
        Holder<Enchantment> targetEnchantment = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment);
        Predicate<ItemStack> predicate = stack -> stack.getItem() == Items.SPYGLASS && EnchantmentHelper.getItemEnchantmentLevel(targetEnchantment, stack) > 0;
        return capability.isEquipped(predicate);
    }
}
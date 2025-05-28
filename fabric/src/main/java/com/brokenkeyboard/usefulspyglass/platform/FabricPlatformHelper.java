package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.Trinkets;
import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import com.brokenkeyboard.usefulspyglass.network.packet.SpyglassEnchPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.function.Predicate;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public EnchantmentCategory getSpyglassEnchCategory() {
        return EnchantmentCategory.BREAKABLE;
    }

    @Override
    public boolean hasSpyglassEnchant(Player player, Enchantment enchantment) {
        ItemStack stack = player.getUseItem();
        Predicate<ItemStack> predicate = stack1 -> stack1.getItem() == Items.SPYGLASS && EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack1) > 0;

        return (EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack) > 0) ||
                (FabricLoader.getInstance().isModLoaded("trinkets") && Trinkets.checkTrinkets(player, predicate) && !ServerHandler.usingPrecision(player) && stack.getItem() != Items.SPYGLASS);
    }

    @Override
    public boolean hasPrecision(Player player) {
        Predicate<ItemStack> predicate = stack1 -> stack1.getItem() == Items.SPYGLASS && EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.PRECISION, stack1) > 0;

        return (EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.PRECISION, player.getItemInHand(InteractionHand.OFF_HAND)) > 0
                || (FabricLoader.getInstance().isModLoaded("trinkets") && Trinkets.checkTrinkets(player, predicate)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean testPrecisionCompat(Player player) {
        ItemStack stack = player.isUsingItem() ? player.getUseItem() : player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(stack.getItem() instanceof ProjectileWeaponItem)) return false;

        if (FabricLoader.getInstance().getObjectShare().get(ModRegistry.MOD_ID + ":" + stack.getItem()) instanceof Predicate<?> predicate) {
            try {
                return ((Predicate<LivingEntity>) predicate).test(player);
            } catch (ClassCastException exception) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void useSpyglassEnch() {
        ClientPlayNetworking.send(new SpyglassEnchPacket());
    }
}
package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.AccessoriesHandler;
import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import com.brokenkeyboard.usefulspyglass.network.SpyglassEnchPayload;
import com.brokenkeyboard.usefulspyglass.network.ServerHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public boolean hasSpyglass(Player player, ResourceKey<Enchantment> enchant) {
        ItemStack stack = player.getUseItem();
        return (EnchantmentHelper.getItemEnchantmentLevel(player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchant), stack) > 0)
                || (FabricLoader.getInstance().isModLoaded("accessories") && AccessoriesHandler.checkAccessories(player, enchant) && !ServerHandler.usingPrecision(player) && stack.getItem() != Items.SPYGLASS);
    }

    @Override
    public boolean hasPrecision(Player player) {
        return (EnchantmentHelper.getItemEnchantmentLevel(player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModRegistry.PRECISION), player.getItemInHand(InteractionHand.OFF_HAND)) > 0)
                || (FabricLoader.getInstance().isModLoaded("accessories") && AccessoriesHandler.checkAccessories(player, ModRegistry.PRECISION));
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
        ClientPlayNetworking.send(new SpyglassEnchPayload());
    }

    @Override
    public void setPrecisionBonus(Projectile projectile) {
        projectile.setAttached(UsefulSpyglass.PRECISION_BONUS, 1.2D);
    }

    @Override
    public double getPrecisionBonus(Projectile projectile) {
        Optional<Double> bonus = Optional.ofNullable(projectile.getAttached(UsefulSpyglass.PRECISION_BONUS));
        return bonus.orElse(1D);
    }
}
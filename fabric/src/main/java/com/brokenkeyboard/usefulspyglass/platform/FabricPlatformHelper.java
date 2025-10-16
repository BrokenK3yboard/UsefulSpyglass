package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.EntityFinder;
import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.Trinkets;
import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import com.brokenkeyboard.usefulspyglass.api.event.BlockTooltipCallback;
import com.brokenkeyboard.usefulspyglass.api.event.LivingTooltipCallback;
import com.brokenkeyboard.usefulspyglass.network.ServerHandler;
import com.brokenkeyboard.usefulspyglass.network.SpyglassEnchPayload;
import com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.api.model.PickContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public boolean hasSpyglassEnchant(Player player, ResourceKey<Enchantment> enchant) {
        ItemStack stack = player.getUseItem();
        Holder<Enchantment> targetEnchantment = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchant);
        Predicate<ItemStack> predicate = stack1 -> stack1.getItem() == Items.SPYGLASS && EnchantmentHelper.getItemEnchantmentLevel(targetEnchantment, stack1) > 0;

        return (EnchantmentHelper.getItemEnchantmentLevel(player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchant), stack) > 0)
                || (FabricLoader.getInstance().isModLoaded("trinkets") && Trinkets.checkTrinkets(player, predicate) && !ServerHandler.usingPrecision(player) && stack.getItem() != Items.SPYGLASS);
    }

    @Override
    public boolean hasPrecision(Player player) {
        Holder<Enchantment> targetEnchantment = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModRegistry.PRECISION);
        Predicate<ItemStack> predicate = stack1 -> stack1.getItem() == Items.SPYGLASS && EnchantmentHelper.getItemEnchantmentLevel(targetEnchantment, stack1) > 0;

        return (EnchantmentHelper.getItemEnchantmentLevel(player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModRegistry.PRECISION), player.getItemInHand(InteractionHand.OFF_HAND)) > 0)
                || (FabricLoader.getInstance().isModLoaded("trinkets") && Trinkets.checkTrinkets(player, predicate));
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
    public HitResult getHitResult(Camera camera, float partialTick, Player player) {
        if (FabricLoader.getInstance().isModLoaded("trinkets") && ShoulderSurfing.getInstance().isShoulderSurfing()) {
            PickContext pickContext = new PickContext.Builder(camera).withEntity(player).withFluidContext(ClipContext.Fluid.NONE).build();
            return ShoulderSurfing.getInstance().getObjectPicker().pick(pickContext, 100, partialTick, player);
        }
        return EntityFinder.getAimedObject(player.level(), camera.getEntity(), camera.getPosition(), camera.getEntity().getViewVector(partialTick));
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

    @Override
    public void livingTooltipCallback(LivingEntity entity, List<ClientTooltipComponent> eventTooltips) {
        LivingTooltipCallback.EVENT.invoker().livingTooltipCallback(entity, eventTooltips);
    }

    @Override
    public void blockTooltipCallback(BlockState state, BlockPos pos, List<ClientTooltipComponent> eventTooltips) {
        BlockTooltipCallback.EVENT.invoker().blockTooltipEvent(state, pos, eventTooltips);
    }
}
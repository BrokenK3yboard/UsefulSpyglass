package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.Curios;
import com.brokenkeyboard.usefulspyglass.EntityFinder;
import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import com.brokenkeyboard.usefulspyglass.api.event.BlockTooltipEvent;
import com.brokenkeyboard.usefulspyglass.api.event.LivingTooltipEvent;
import com.brokenkeyboard.usefulspyglass.network.SpyglassEnchPayload;
import com.brokenkeyboard.usefulspyglass.network.ServerHandler;
import com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.api.model.PickContext;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Predicate;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean hasSpyglassEnchant(Player player, ResourceKey<Enchantment> enchant) {
        ItemStack stack = player.getUseItem();
        Holder<Enchantment> targetEnchantment = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchant);
        Predicate<ItemStack> predicate = stack1 -> stack1.getItem() == Items.SPYGLASS && EnchantmentHelper.getTagEnchantmentLevel(targetEnchantment, stack1) > 0;

        return (EnchantmentHelper.getTagEnchantmentLevel(player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchant), stack) > 0)
                || (ModList.get().isLoaded("curios") && Curios.checkCurios(player, predicate) && !ServerHandler.usingPrecision(player)
                && stack.getItem() != Items.SPYGLASS);
    }

    @Override
    public boolean hasPrecision(Player player) {
        Holder<Enchantment> targetEnchantment = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModRegistry.PRECISION);
        Predicate<ItemStack> predicate = stack1 -> stack1.getItem() == Items.SPYGLASS && EnchantmentHelper.getTagEnchantmentLevel(targetEnchantment, stack1) > 0;

        return (EnchantmentHelper.getTagEnchantmentLevel(player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModRegistry.PRECISION),
                player.getItemInHand(InteractionHand.OFF_HAND)) > 0)
                || (ModList.get().isLoaded("curios") && Curios.checkCurios(player, predicate));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean testPrecisionCompat(Player player) {
        for (Predicate<?> predicate : UsefulSpyglass.PRECISION_COMPATIBLE) {
            try {
                if (((Predicate<LivingEntity>) predicate).test(player)) return true;
            } catch (ClassCastException exception) {
                return false;
            }
        }
        return false;
    }

    @Override
    public HitResult getHitResult(Camera camera, float partialTick, Player player) {
        if (ModList.get().isLoaded("shouldersurfing") && ShoulderSurfing.getInstance().isShoulderSurfing()) {
            PickContext pickContext = new PickContext.Builder(camera).withEntity(player).withFluidContext(ClipContext.Fluid.NONE).build();
            return ShoulderSurfing.getInstance().getObjectPicker().pick(pickContext, 100, partialTick, player);
        }
        return EntityFinder.getAimedObject(player.level(), camera.getEntity(), camera.getPosition(), camera.getEntity().getViewVector(partialTick));
    }

    @Override
    public void useSpyglassEnch() {
        PacketDistributor.sendToServer(new SpyglassEnchPayload());
    }

    @Override
    public void setPrecisionBonus(Projectile projectile) {
        projectile.setData(UsefulSpyglass.PRECISION_BONUS, 1.2D);
    }

    @Override
    public double getPrecisionBonus(Projectile projectile) {
        return projectile.getData(UsefulSpyglass.PRECISION_BONUS);
    }

    @Override
    public void livingTooltipCallback(LivingEntity entity, List<ClientTooltipComponent> eventTooltips) {
        LivingTooltipEvent event = new LivingTooltipEvent(entity, eventTooltips);
        NeoForge.EVENT_BUS.post(event);
    }

    @Override
    public void blockTooltipCallback(BlockState state, BlockPos pos, List<ClientTooltipComponent> tooltipInfoList) {
        BlockTooltipEvent event = new BlockTooltipEvent(state, pos, tooltipInfoList);
        NeoForge.EVENT_BUS.post(event);
    }
}
package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.EntityFinder;
import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.Trinkets;
import com.brokenkeyboard.usefulspyglass.api.event.BlockTooltipCallback;
import com.brokenkeyboard.usefulspyglass.api.event.LivingTooltipCallback;
import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import com.brokenkeyboard.usefulspyglass.network.packet.SpyglassEnchPacket;
import com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.api.model.PickContext;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
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
    public HitResult getHitResult(Camera camera, float partialTick, Player player) {
        if (FabricLoader.getInstance().isModLoaded("shouldersurfing") && ShoulderSurfing.getInstance().isShoulderSurfing()) {
            PickContext pickContext = new PickContext.Builder(camera).withEntity(player).withFluidContext(ClipContext.Fluid.NONE).build();
            return ShoulderSurfing.getInstance().getObjectPicker().pick(pickContext, 100, partialTick, Minecraft.getInstance().gameMode);
        }
        return EntityFinder.getAimedObject(player.level(), camera.getEntity(), camera.getPosition(), camera.getEntity().getViewVector(partialTick));
    }

    @Override
    public void useSpyglassEnch() {
        ClientPlayNetworking.send(new SpyglassEnchPacket());
    }

    @Override
    public void livingTooltipCallback(LivingEntity entity, List<ClientTooltipComponent> eventTooltips) {
        LivingTooltipCallback.EVENT.invoker().livingTooltipCallback(entity, eventTooltips);
    }

    @Override
    public void blockTooltipCallback(BlockState state, BlockPos pos, List<ClientTooltipComponent> eventTooltips) {
        BlockTooltipCallback.EVENT.invoker().blockTooltipEvent(state, pos, eventTooltips);
    }

    @Override
    public ConcurrentMap<String, String> getModList() {
        ConcurrentMap<String, String> map = Maps.newConcurrentMap();
        FabricLoader.getInstance().getAllMods().forEach(modContainer ->
                map.put(modContainer.getMetadata().getId(), modContainer.getMetadata().getName()));
        return map;
    }
}
package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.Curios;
import com.brokenkeyboard.usefulspyglass.EntityFinder;
import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import com.brokenkeyboard.usefulspyglass.api.event.BlockTooltipEvent;
import com.brokenkeyboard.usefulspyglass.api.event.LivingTooltipEvent;
import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import com.brokenkeyboard.usefulspyglass.network.PacketHandler;
import com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.api.model.PickContext;
import com.google.common.collect.Maps;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public EnchantmentCategory getSpyglassEnchCategory() {
        return EnchantmentCategory.create("spyglass", item -> item instanceof SpyglassItem);
    }

    @Override
    public boolean hasSpyglassEnchant(Player player, Enchantment enchantment) {
        ItemStack stack = player.getUseItem();
        Predicate<ItemStack> predicate = stack1 -> stack1.getItem() == Items.SPYGLASS && EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack1) > 0;

        return (EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack) > 0) ||
                (ModList.get().isLoaded("curios") && Curios.checkCurios(player, predicate) && !ServerHandler.usingPrecision(player)
                && stack.getItem() != Items.SPYGLASS);
    }

    @Override
    public boolean hasPrecision(Player player) {
        Predicate<ItemStack> predicate = stack1 -> stack1.getItem() == Items.SPYGLASS && EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.PRECISION, stack1) > 0;

        return (EnchantmentHelper.getTagEnchantmentLevel(ModRegistry.PRECISION, player.getItemInHand(InteractionHand.OFF_HAND)) > 0 ||
                (ModList.get().isLoaded("curios") && Curios.checkCurios(player, predicate)));
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
            return ShoulderSurfing.getInstance().getObjectPicker().pick(pickContext, 100, partialTick, Minecraft.getInstance().gameMode);
        }
        return EntityFinder.getAimedObject(player.level(), camera.getEntity(), camera.getPosition(), camera.getEntity().getViewVector(partialTick));
    }

    @Override
    public void useSpyglassEnch() {
        PacketHandler.sendSpyglassPacket();
    }

    @Override
    public void livingTooltipCallback(LivingEntity entity, List<ClientTooltipComponent> eventTooltips) {
        LivingTooltipEvent event = new LivingTooltipEvent(entity, eventTooltips);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public void blockTooltipCallback(BlockState state, BlockPos pos, List<ClientTooltipComponent> tooltipInfoList) {
        BlockTooltipEvent event = new BlockTooltipEvent(state, pos, tooltipInfoList);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public ConcurrentMap<String, String> getModList() {
        ConcurrentMap<String, String> map = Maps.newConcurrentMap();
        List<IModInfo> list = ModList.get().getMods();
        for (IModInfo mod : list) {
            String modid = mod.getModId();
            String name = mod.getDisplayName() != null ? mod.getDisplayName() : modid;
            map.put(modid, name);
        }
        return map;
    }
}
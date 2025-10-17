package com.brokenkeyboard.usefulspyglass.platform;

import net.minecraft.client.Camera;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface IPlatformHelper {
    EnchantmentCategory getSpyglassEnchCategory();
    boolean hasSpyglassEnchant(Player player, Enchantment enchantment);
    boolean hasPrecision(Player player);
    boolean testPrecisionCompat(Player player);
    HitResult getHitResult(Camera camera, float partialTick, Player player);
    void useSpyglassEnch();
    void livingTooltipCallback(LivingEntity entity, List<ClientTooltipComponent> eventTooltips);
    void blockTooltipCallback(BlockState state, BlockPos pos, List<ClientTooltipComponent> eventTooltips);
    ConcurrentMap<String, String> getModList();
}
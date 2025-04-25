package com.brokenkeyboard.usefulspyglass.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public interface IPlatformHelper {
    boolean shouldRenderBlock();
    EnchantmentCategory getSpyglassEnchCategory();
    boolean hasMarkingSpyglass(Player player);
    boolean hasPrecisionSpyglass(Player player);
    void sendMarkingPacket(int entityID, ResourceLocation dimension);
}
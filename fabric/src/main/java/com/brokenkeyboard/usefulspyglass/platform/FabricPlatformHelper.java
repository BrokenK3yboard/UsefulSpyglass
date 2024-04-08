package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.ClientEvents;
import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import com.brokenkeyboard.usefulspyglass.Trinkets;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import static com.brokenkeyboard.usefulspyglass.Constants.MARKING;
import static com.brokenkeyboard.usefulspyglass.Constants.PRECISION;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public EnchantmentCategory getSpyglassEnchCategory() {
        return EnchantmentCategory.BREAKABLE;
    }

    @Override
    public boolean hasMarkingSpyglass(Player player) {
        ItemStack stack = player.getUseItem();
        return MARKING.test(player.getUseItem()) || (FabricLoader.getInstance().isModLoaded("trinkets") && Trinkets.checkTrinkets(player, MARKING) &&
                !ServerHandler.usingPrecision(player) && stack.getItem() != Items.SPYGLASS);
    }

    @Override
    public boolean hasPrecisionSpyglass(Player player) {
        return PRECISION.test(player.getItemInHand(InteractionHand.OFF_HAND)) || (FabricLoader.getInstance().isModLoaded("trinkets") && Trinkets.checkTrinkets(player, PRECISION));
    }

    @Override
    public void sendMarkingPacket(int entityID, ResourceLocation dimension) {
        ClientEvents.sendMarkingPacket(entityID, dimension);
    }
}
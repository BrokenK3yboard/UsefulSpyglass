package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.Curios;
import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import com.brokenkeyboard.usefulspyglass.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.ModList;

import static com.brokenkeyboard.usefulspyglass.Constants.MARKING;
import static com.brokenkeyboard.usefulspyglass.Constants.PRECISION;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public EnchantmentCategory getSpyglassEnchCategory() {
        return EnchantmentCategory.create("spyglass", item -> item instanceof SpyglassItem);
    }

    @Override
    public boolean hasMarkingSpyglass(Player player) {
        ItemStack stack = player.getUseItem();
        return MARKING.test(stack) || (ModList.get().isLoaded("curios") && Curios.checkCurios(player, MARKING) && !ServerHandler.usingPrecision(player) &&
                stack.getItem() != Items.SPYGLASS);
    }

    @Override
    public boolean hasPrecisionSpyglass(Player player) {
        return PRECISION.test(player.getItemInHand(InteractionHand.OFF_HAND)) || (ModList.get().isLoaded("curios") && Curios.checkCurios(player, PRECISION));
    }

    @Override
    public void sendMarkingPacket(int entityID, ResourceLocation dimension) {
        PacketHandler.sendMarkingPacket(entityID, dimension);
    }
}
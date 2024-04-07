package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.Trinkets;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import static com.brokenkeyboard.usefulspyglass.Constants.PRECISION_SPYGLASS;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public EnchantmentCategory getSpyglassEnchCategory() {
        return EnchantmentCategory.BREAKABLE;
    }

    @Override
    public boolean checkPrecisionSpyglass(Player player) {
        return !player.getCooldowns().isOnCooldown(Items.SPYGLASS) && (PRECISION_SPYGLASS.test(player.getItemInHand(InteractionHand.OFF_HAND)) ||
                (FabricLoader.getInstance().isModLoaded("trinkets") && Trinkets.checkTrinkets(player)));
    }
}
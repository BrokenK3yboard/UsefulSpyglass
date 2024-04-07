package com.brokenkeyboard.usefulspyglass.platform;

import com.brokenkeyboard.usefulspyglass.Curios;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.ModList;

import static com.brokenkeyboard.usefulspyglass.Constants.PRECISION_SPYGLASS;
import static com.brokenkeyboard.usefulspyglass.UsefulSpyglass.SPYGLASS;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public EnchantmentCategory getSpyglassEnchCategory() {
        return SPYGLASS;
    }

    @Override
    public boolean checkPrecisionSpyglass(Player player) {
        return !player.getCooldowns().isOnCooldown(Items.SPYGLASS) && (PRECISION_SPYGLASS.test(player.getItemInHand(InteractionHand.OFF_HAND)) ||
                (ModList.get().isLoaded("curios") && Curios.checkCurios(player)));
    }
}
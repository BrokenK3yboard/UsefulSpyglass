package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.CommonConfig;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, priority = 900)
public class PlayerMixin {

    @Inject(method = "isScoping", at = @At(value = "RETURN"), cancellable = true)
    private void isScoping(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        boolean isCrossbow = stack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(stack);
        boolean isBow = CommonConfig.PRECISION_BOWS.get() && player.getUseItem().getItem() == Items.BOW && (BowItem.getPowerForTime(player.getTicksUsingItem()) >= 1.0F);
        boolean hasSpyglass = Services.PLATFORM.checkPrecisionSpyglass(player);
        if ((isBow || isCrossbow) && hasSpyglass && player.isCrouching()) {
            cir.setReturnValue(true);
        }
    }
}
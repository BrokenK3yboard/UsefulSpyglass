package com.brokenkeyboard.usefulspyglass.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
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

@Mixin(value = AbstractClientPlayer.class, priority = 1100)
public class AbstractClientPlayerMixin {

    @Inject(method = "getFieldOfViewModifier", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void modifyFOV(CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        boolean isCrossbow = stack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(stack);
        boolean isBow = player.getUseItem().getItem() == Items.BOW && BowItem.getPowerForTime(player.getTicksUsingItem()) >= 1.0F;
        if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.isScoping() && (isCrossbow || isBow)) {
            cir.setReturnValue(0.1F);
        }
    }
}
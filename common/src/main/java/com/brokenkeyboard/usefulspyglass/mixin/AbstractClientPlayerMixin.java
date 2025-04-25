package com.brokenkeyboard.usefulspyglass.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractClientPlayer.class, priority = 1100)
public class AbstractClientPlayerMixin {

    @Inject(method = "getFieldOfViewModifier", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void modifyFOV(CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.isScoping()) {
            cir.setReturnValue(0.1F);
        }
    }
}
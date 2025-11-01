package com.brokenkeyboard.usefulspyglass.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @ModifyReturnValue(method = "getFieldOfViewModifier", at = @At("RETURN"))
    public float modifyFOV(float original) {
        Player player = (Player) (Object) this;
        if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.isScoping()) {
            return 0.1F;
        }
        return original;
    }
}
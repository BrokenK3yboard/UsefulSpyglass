package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class)
public class PlayerMixin {

    @Inject(method = "isScoping", at = @At(value = "RETURN"), cancellable = true)
    private void isScoping(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (ServerHandler.usingPrecision(player)) {
            cir.setReturnValue(true);
        }
    }
}
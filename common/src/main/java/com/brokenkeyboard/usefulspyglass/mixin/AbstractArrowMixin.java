package com.brokenkeyboard.usefulspyglass.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @Shadow
    private int life;

    @Shadow
    protected boolean inGround;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;tick()V", shift = At.Shift.AFTER))
    private void removeArrow(CallbackInfo ci) {
        Projectile projectile = (Projectile) (Object) this;
        if (projectile.getTags().contains("precision") && life > 1200 && !this.inGround) {
            projectile.discard();
        }
    }
}
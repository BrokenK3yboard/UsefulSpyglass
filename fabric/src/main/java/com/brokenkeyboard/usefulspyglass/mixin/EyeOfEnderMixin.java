package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.SpotterEye;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EyeOfEnder.class)
public class EyeOfEnderMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/EyeOfEnder;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void modifyVelocity(EyeOfEnder instance, Vec3 vec3, Operation<Void> original, @Local(ordinal = 1) double d1, @Local(ordinal = 6) double d6, @Local(ordinal = 0) float f) {
        if (instance instanceof SpotterEye eye) {
            double vecY = eye.inPosition ? vec3.y() : Math.tan(Math.atan2(((EyeOfEnderAccessor)eye).getTy() - d1, f)) * d6;
            Vec3 newVec = new Vec3(Mth.clamp(vec3.x(), -2, 2), Mth.clamp(vecY, -2, 2), Mth.clamp(vec3.z(), -2, 2));
            original.call(instance, newVec);
        } else {
            original.call(instance, vec3);
        }
    }
}

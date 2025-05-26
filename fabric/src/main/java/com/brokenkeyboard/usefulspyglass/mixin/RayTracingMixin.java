package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import snownee.jade.overlay.RayTracing;

@Pseudo
@Mixin(RayTracing.class)
public class RayTracingMixin {

    @WrapOperation(method = "fire", at = @At(value = "INVOKE", target = "Lsnownee/jade/overlay/RayTracing;rayTrace(Lnet/minecraft/world/entity/Entity;DD)Lnet/minecraft/world/phys/HitResult;"), require = 0)
    private HitResult modifyRange(RayTracing instance, Entity lookVector, double entityDist, double blockDist, Operation<HitResult> original, @Local Player player) {
        return ClientConfig.JADE_INTEGRATION.get() && player.isScoping() ? original.call(instance, lookVector, 100D, 100D) : original.call(instance, lookVector, entityDist, blockDist);
    }
}

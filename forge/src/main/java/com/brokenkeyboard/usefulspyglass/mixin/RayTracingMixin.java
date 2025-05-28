package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

    @WrapOperation(method = "fire", at = @At(value = "INVOKE", target = "Lsnownee/jade/overlay/RayTracing;rayTrace(Lnet/minecraft/world/entity/Entity;D)Lnet/minecraft/world/phys/HitResult;"), require = 0, remap = false)
    private HitResult modifyRange(RayTracing instance, Entity viewpoint, double distance, Operation<HitResult> original) {
        return ClientConfig.JADE_INTEGRATION.get() && viewpoint instanceof Player player && player.isScoping() ? original.call(instance, viewpoint, 100D) : original.call(instance, viewpoint, distance);
    }
}

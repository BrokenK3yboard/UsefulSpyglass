package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @WrapOperation(method = "shootProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V"))
    private static void setDeviation(Projectile projectile, double vecX, double vecY, double vecZ, float shootingPower, float deviation, Operation<Void> original,
                                     @Local(ordinal = 0, argsOnly = true) LivingEntity entity) {
        original.call(projectile, vecX, vecY, vecZ, shootingPower, ServerHandler.handlePrecision(entity, projectile, deviation));
    }
}
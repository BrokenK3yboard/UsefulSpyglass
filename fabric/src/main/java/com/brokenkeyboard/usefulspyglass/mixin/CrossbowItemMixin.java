package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.CommonConfig;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @WrapOperation(method = "shootProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V"))
    private static void setDeviation(Projectile instance, double vecX, double vecY, double vecZ, float shootingPower, float deviation, Operation<Void> original,
                                     @Local(ordinal = 0, argsOnly = true) LivingEntity entity) {
        if (entity instanceof Player player && player.isScoping() && Services.PLATFORM.checkPrecisionSpyglass(player)) {
            instance.setNoGravity(true);
            original.call(instance, vecX, vecY, vecZ, shootingPower, 0F);
            player.getCooldowns().addCooldown(Items.SPYGLASS, CommonConfig.PRECISION_COOLDOWN.get());
        } else {
            original.call(instance, vecX, vecY, vecZ, shootingPower, deviation);
        }
    }
}
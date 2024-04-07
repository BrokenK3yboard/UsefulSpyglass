package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.CommonConfig;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public class BowItemMixin {

    @WrapOperation(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private void setDeviation(AbstractArrow instance, Entity entity, float f1, float f2, float f3, float f4, float f5, Operation<Void> original, @Local(ordinal = 0) float power) {
        if (entity instanceof Player player) {
            if (CommonConfig.PRECISION_BOWS.get() && player.isScoping() && Services.PLATFORM.checkPrecisionSpyglass(player)) {
                instance.setNoGravity(true);
                original.call(instance, entity, f1, f2, f3, f4, 0F);
                player.getCooldowns().addCooldown(Items.SPYGLASS, CommonConfig.PRECISION_COOLDOWN.get());
            } else {
                original.call(instance, entity, f1, f2, f3, f4, f5);
            }
        }
    }
}
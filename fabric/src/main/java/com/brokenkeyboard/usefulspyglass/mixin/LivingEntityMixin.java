package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyVariable(method = "hurt", at = @At(value = "HEAD"), argsOnly = true)
    public float modifyDamage(float amount, @Local(argsOnly = true) DamageSource source) {
        if (source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() instanceof Projectile projectile) {
            Optional<Double> bonus = Optional.ofNullable(projectile.getAttached(UsefulSpyglass.PRECISION_BONUS));
            return (float) (amount * bonus.orElse(1D));
        }
        return amount;
    }
}
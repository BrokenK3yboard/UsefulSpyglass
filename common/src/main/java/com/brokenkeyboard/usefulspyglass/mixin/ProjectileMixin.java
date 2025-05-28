package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Projectile.class)
public abstract class ProjectileMixin {

    @Shadow @Nullable public abstract Entity getOwner();

    @Inject(method = "shoot", at = @At(value = "HEAD"))
    private void shootProjectile(double x, double y, double z, float velocity, float inaccuracy, CallbackInfo ci) {
        if (this.getOwner() instanceof Player player && !player.getCooldowns().isOnCooldown(Items.SPYGLASS) && player.isCrouching() && Services.PLATFORM.hasPrecision(player)) {
            Projectile projectile = ((Projectile) (Object) this);
            projectile.setNoGravity(true);
            projectile.addTag("precision");
            player.getCooldowns().addCooldown(Items.SPYGLASS, CommonConfig.PRECISION_COOLDOWN.get());
        }
    }
}

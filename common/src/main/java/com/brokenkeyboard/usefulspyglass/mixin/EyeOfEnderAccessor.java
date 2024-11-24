package com.brokenkeyboard.usefulspyglass.mixin;

import net.minecraft.world.entity.projectile.EyeOfEnder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EyeOfEnder.class)
public interface EyeOfEnderAccessor {
    @Accessor
    void setTx(double tx);

    @Accessor
    void setTy(double ty);

    @Accessor
    void setTz(double tz);

    @Accessor
    void setLife(int life);

    @Accessor
    void setSurviveAfterDeath(boolean surviveAfterDeath);

    @Accessor
    double getTy();

    @Accessor
    int getLife();
}

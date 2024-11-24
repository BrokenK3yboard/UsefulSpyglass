package com.brokenkeyboard.usefulspyglass.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor
    static EntityDataAccessor<Byte> getDATA_SHARED_FLAGS_ID() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static int getFLAG_GLOWING() {
        throw new UnsupportedOperationException();
    }
}

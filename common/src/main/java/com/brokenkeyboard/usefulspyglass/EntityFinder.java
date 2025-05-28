package com.brokenkeyboard.usefulspyglass;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public class EntityFinder {

    public static HitResult getAimedObject(Level level, Entity entity, Vec3 start, Vec3 viewVector) {
        if (level == null || entity == null) return null;

        Vec3 end = start.add(viewVector.scale(100));
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(entity, start, end, new AABB(start, end), EntitySelector.LIVING_ENTITY_STILL_ALIVE, 0f);
        BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));

        if (entityHit != null && !entityHit.getEntity().isInvisible()) {
            double blockDistance = blockHit.getLocation().distanceToSqr(start);
            double entityDistance = entityHit.getLocation().distanceToSqr(start);
            return entityDistance < blockDistance ? entityHit : blockHit;
        } else if (blockHit.getType() == HitResult.Type.BLOCK) {
            return blockHit;
        }
        return null;
    }
}
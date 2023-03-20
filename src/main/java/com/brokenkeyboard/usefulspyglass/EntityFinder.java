package com.brokenkeyboard.usefulspyglass;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

public class EntityFinder {

    public static HitResult getAimedEntity(Minecraft client, double distance) {
        Entity camera = client.getCameraEntity();

        if (camera == null) return null;

        Vec3 viewVec = camera.getViewVector(client.getFrameTime());
        Vec3 start = camera.getEyePosition(client.getFrameTime());
        Vec3 end = start.add(viewVec.x * distance, viewVec.y * distance, viewVec.z * distance);

        BlockHitResult blockHit = camera.level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, camera));
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(camera, start, end, new AABB(start, end), EntitySelector.ENTITY_STILL_ALIVE, 0f);

        if (entityHit != null) {
            if (blockHit.getType() == HitResult.Type.MISS) {
                return entityHit;
            }

            double blockDistance = blockHit.getLocation().distanceToSqr(start);
            double entityDistance = entityHit.getLocation().distanceToSqr(start);

            if (entityDistance < blockDistance) {
                return entityHit;
            }
        }
        return blockHit;
    }
}
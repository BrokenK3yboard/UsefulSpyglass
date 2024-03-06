package com.brokenkeyboard.usefulspyglass;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

public class EntityFinder {

    public static HitResult getAimedObject(Minecraft client, double distance) {
        Entity camera = client.getCameraEntity();
        if (camera == null || client.level == null) return null;

        Vec3 viewVec = camera.getViewVector(client.getFrameTime());
        Vec3 start = camera.getEyePosition(client.getFrameTime());
        Vec3 end = start.add(viewVec.x * distance, viewVec.y * distance, viewVec.z * distance);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(camera, start, end, new AABB(start, end), EntitySelector.LIVING_ENTITY_STILL_ALIVE, 0f);
        BlockHitResult blockHit = camera.level().clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, camera));
        BlockState state = client.level.getBlockState(blockHit.getBlockPos());

        if (entityHit != null && !entityHit.getEntity().isInvisible()) {
            double blockDistance = blockHit.getLocation().distanceToSqr(start);
            double entityDistance = entityHit.getLocation().distanceToSqr(start);
            return entityDistance < blockDistance || blockHit.getType() == HitResult.Type.MISS || state.isAir() ? entityHit : blockHit;
        } else if (blockHit.getType() != HitResult.Type.MISS && !state.isAir()) {
            return blockHit;
        }
        return null;
    }
}
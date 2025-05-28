package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.mixin.EyeOfEnderAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpotterEye extends EyeOfEnder {

    private ServerPlayer owner;
    private Vec3 position;
    public boolean inPosition = false;

    public SpotterEye(EntityType<? extends SpotterEye> type, Level level) {
        super(type, level);
    }

    public SpotterEye(Level level, double x, double y, double z, ServerPlayer owner) {
        super(level, x, y, z);
        this.owner = owner;
    }

    @Override
    public void signalTo(BlockPos pos) {
        ((EyeOfEnderAccessor)this).setTx(pos.getX());
        ((EyeOfEnderAccessor)this).setTy(pos.getY());
        ((EyeOfEnderAccessor)this).setTz(pos.getZ());
        ((EyeOfEnderAccessor)this).setLife(80 - CommonConfig.SPOTTER_DURATION.get());
        ((EyeOfEnderAccessor)this).setSurviveAfterDeath(false);
        position = pos.getCenter();
    }

    @Override
    public void tick() {
        super.tick();
        if (inPosition && this.level() instanceof ServerLevel && this.owner != null && ((EyeOfEnderAccessor)this).getLife() % 80 == 0) {
            int range = CommonConfig.SPOTTER_RANGE.get();
            AABB aabb = new AABB(this.getX() - range, this.getY() - range, this.getZ() - range,
                    this.getX() + range, this.getY() + range, this.getZ() + range);

            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
            entities.removeIf(entity -> !hasLOS(level(), entity.getEyePosition(), this.position(), this));

            for (LivingEntity entity : entities) {
                ((MarkedEntitiesAccess)this.owner).us$getMarkedEntities().addEntity(entity, 100);
            }
        } else if (!inPosition && this.position().distanceTo(position) < 1) {
            inPosition = true;
        }
    }

    private static boolean hasLOS(Level level, Vec3 startPos, Vec3 targetPos, Entity entity) {
        BlockHitResult result = level.clip(new ClipContext(startPos, targetPos, ClipContext.Block.VISUAL, ClipContext.Fluid.ANY, entity));
        return result.getBlockPos().equals(BlockPos.containing(targetPos)) || result.getType() == HitResult.Type.MISS;
    }
}
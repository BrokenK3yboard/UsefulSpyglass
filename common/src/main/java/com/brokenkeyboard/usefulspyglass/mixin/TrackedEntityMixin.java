package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.MarkedEntitiesAccess;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.TrackedEntity.class)
public class TrackedEntityMixin {

    @Shadow @Final
    Entity entity;

    @Inject(method = "broadcast", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerConnection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void modifyPacket(Packet<?> packet, CallbackInfo ci, @Local ServerPlayerConnection connection) {
        if (packet instanceof ClientboundSetEntityDataPacket entityPacket) {
            if (((MarkedEntitiesAccess) connection.getPlayer()).us$getMarkedEntities().getMarkedEntities().containsKey(entity.getUUID())) {
                byte entityData = this.entity.getEntityData().get(EntityAccessor.getDATA_SHARED_FLAGS_ID());
                entityPacket.packedItems().add(new SynchedEntityData.DataValue<>(EntityAccessor.getDATA_SHARED_FLAGS_ID().id(),
                        EntityAccessor.getDATA_SHARED_FLAGS_ID().serializer(), (byte) (entityData | 1 << EntityAccessor.getFLAG_GLOWING())));
            }
        }
    }
}
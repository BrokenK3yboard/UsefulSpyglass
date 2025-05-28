package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.MarkedEntitiesAccess;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

    @Shadow
    @Final
    private Entity entity;

    @ModifyExpressionValue(method = "sendPairingData", at = @At(value = "NEW", args = "class=net/minecraft/network/protocol/game/ClientboundSetEntityDataPacket"))
    private ClientboundSetEntityDataPacket us$modifyPacket(ClientboundSetEntityDataPacket packet, ServerPlayer player) {
        if (((MarkedEntitiesAccess) player).us$getMarkedEntities().getMarkedEntities().containsKey(entity.getUUID())) {
            byte entityData = entity.getEntityData().get(EntityAccessor.getDATA_SHARED_FLAGS_ID());
            packet.packedItems().add(new SynchedEntityData.DataValue<>(EntityAccessor.getDATA_SHARED_FLAGS_ID().hashCode(),
                    EntityAccessor.getDATA_SHARED_FLAGS_ID().getSerializer(), (byte) (entityData | 1 << EntityAccessor.getFLAG_GLOWING())));
        }
        return packet;
    }
}

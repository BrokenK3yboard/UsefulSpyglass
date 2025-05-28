package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.mixin.EntityAccessor;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.*;

public class MarkedEntities {

    private final Map<UUID, Integer> MARKED_ENTITIES = new HashMap<>();
    private final ServerPlayer PLAYER;
    private int tickCount;

    public MarkedEntities(ServerPlayer player) {
        this.PLAYER = player;
    }

    public void tick() {
        ++this.tickCount;
        if (!this.MARKED_ENTITIES.isEmpty()) {
            Iterator<Map.Entry<UUID, Integer>> iterator = this.MARKED_ENTITIES.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<UUID, Integer> entry = iterator.next();
                if (entry.getValue() <= this.tickCount) {
                    Entity entity = PLAYER.serverLevel().getEntity(entry.getKey());
                    iterator.remove();
                    if (entity != null) {
                        PLAYER.connection.send(createPacket(entity, false));
                    }
                }
            }
        }
    }

    public void addEntity(Entity entity, int ticks) {
        MARKED_ENTITIES.put(entity.getUUID(), tickCount + ticks);
        PLAYER.connection.send(createPacket(entity, true));
    }

    public ClientboundSetEntityDataPacket createPacket(Entity entity, boolean shouldGlow) {
        byte entityData = entity.getEntityData().get(EntityAccessor.getDATA_SHARED_FLAGS_ID());
        byte isGlowing = (byte) (shouldGlow ? entityData | 1 << EntityAccessor.getFLAG_GLOWING() : entityData & ~(1 << EntityAccessor.getFLAG_GLOWING()));
        List<SynchedEntityData.DataValue<?>> original = entity.getEntityData().getNonDefaultValues();
        List<SynchedEntityData.DataValue<?>> packedItems = original != null ? original : new ArrayList<>();
        packedItems.add(new SynchedEntityData.DataValue<>(EntityAccessor.getDATA_SHARED_FLAGS_ID().hashCode(), EntityAccessor.getDATA_SHARED_FLAGS_ID().serializer(), isGlowing));
        return new ClientboundSetEntityDataPacket(entity.getId(), packedItems);
    }

    public Map<UUID, Integer> getMarkedEntities() {
        return MARKED_ENTITIES;
    }
}
package com.brokenkeyboard.usefulspyglass.network.packet;

import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MarkEntityPacket implements FabricPacket {

    private final int entityID;
    private final ResourceLocation dimension;

    public MarkEntityPacket(int entityID, ResourceLocation dimension) {
        this.entityID = entityID;
        this.dimension = dimension;
    }

    public MarkEntityPacket(FriendlyByteBuf buf) {
        entityID = buf.readInt();
        dimension = buf.readResourceLocation();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeResourceLocation(dimension);
    }

    @Override
    public PacketType<?> getType() {
        return UsefulSpyglass.MARKING_TYPE;
    }
}
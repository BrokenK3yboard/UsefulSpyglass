package com.brokenkeyboard.usefulspyglass.network;

import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MarkEntityPacket {

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

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeResourceLocation(dimension);
    }

    public static void handle(MarkEntityPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ServerHandler.markEntity(context.getSender(), packet.entityID, packet.dimension));
        supplier.get().setPacketHandled(true);
    }
}
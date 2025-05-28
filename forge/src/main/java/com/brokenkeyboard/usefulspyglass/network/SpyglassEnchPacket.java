package com.brokenkeyboard.usefulspyglass.network;

import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpyglassEnchPacket {

    public SpyglassEnchPacket() {}

    public SpyglassEnchPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public static void handle(SpyglassEnchPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ServerHandler.handleEnchantments(context.getSender()));
        supplier.get().setPacketHandled(true);
    }
}
package com.brokenkeyboard.usefulspyglass.network.packet;

import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

public class SpyglassEnchPacket implements FabricPacket {

    public SpyglassEnchPacket() {}

    public SpyglassEnchPacket(FriendlyByteBuf buf) {}

    @Override
    public void write(FriendlyByteBuf buf) {}

    @Override
    public PacketType<?> getType() {
        return UsefulSpyglass.MARKING_TYPE;
    }
}
package com.brokenkeyboard.usefulspyglass.network;


import com.brokenkeyboard.usefulspyglass.ModRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SpyglassEnchPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SpyglassEnchPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "spyglass_ench"));
    public static final StreamCodec<ByteBuf, SpyglassEnchPayload> STREAM_CODEC = StreamCodec.unit(new SpyglassEnchPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
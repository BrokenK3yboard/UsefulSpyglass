package com.brokenkeyboard.usefulspyglass.network;

import com.brokenkeyboard.usefulspyglass.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static SimpleChannel instance;
    private static int packetID = 0;

    private static int id() {
        return packetID++;
    }

    public static void register() {

        SimpleChannel network = NetworkRegistry.ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "main"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(string -> true)
                .serverAcceptedVersions(string -> true)
                .simpleChannel();

        instance = network;

        network.messageBuilder(SpyglassEnchPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SpyglassEnchPacket::new)
                .encoder(SpyglassEnchPacket::toBytes)
                .consumerMainThread(SpyglassEnchPacket::handle)
                .add();
    }

    public static void sendSpyglassPacket() {
        instance.sendToServer(new SpyglassEnchPacket());
    }
}
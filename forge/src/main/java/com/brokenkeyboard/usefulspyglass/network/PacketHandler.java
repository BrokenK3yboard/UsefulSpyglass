package com.brokenkeyboard.usefulspyglass.network;

import com.brokenkeyboard.usefulspyglass.Constants;
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

        SimpleChannel network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Constants.MOD_ID, "main"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(string -> true)
                .serverAcceptedVersions(string -> true)
                .simpleChannel();

        instance = network;

        network.messageBuilder(MarkEntityPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MarkEntityPacket::new)
                .encoder(MarkEntityPacket::toBytes)
                .consumerMainThread(MarkEntityPacket::handle)
                .add();
    }

    public static void sendMarkingPacket(int entityID, ResourceLocation level) {
        instance.sendToServer(new MarkEntityPacket(entityID, level));
    }
}
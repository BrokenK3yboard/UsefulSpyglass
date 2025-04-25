package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.brokenkeyboard.usefulspyglass.handler.ClientHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import static com.brokenkeyboard.usefulspyglass.InfoOverlay.*;
import static com.brokenkeyboard.usefulspyglass.UsefulSpyglass.MARKING;

public final class ClientEvents implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientHandler::handleClientTick);
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            if (hitResult != null && ((hitResult instanceof EntityHitResult && ClientConfig.DISPLAY_ENTITIES.get()) || (hitResult instanceof BlockHitResult && ClientConfig.DISPLAY_BLOCKS.get()))) {
                DrawOverlay.drawGUI(new PoseStack(), hitResult, tooltipList, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
            }
        });
    }

    public static void sendMarkingPacket(int entityID, ResourceLocation location) {
        FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
        passedData.writeInt(entityID);
        passedData.writeResourceLocation(location);
        ClientPlayNetworking.send(MARKING, passedData);
    }
}
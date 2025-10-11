package com.brokenkeyboard.usefulspyglass.network;

import com.brokenkeyboard.usefulspyglass.DrawOverlay;
import com.brokenkeyboard.usefulspyglass.EntityFinder;
import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class ClientHandler {

    public static void handleClientTick(Minecraft client) {
        if (client.player instanceof Player player && client.gameRenderer.getMainCamera() instanceof Camera camera && player.isScoping()) {
            Entity cameraEntity = camera.getEntity();
            DrawOverlay.hitResult = EntityFinder.getAimedObject(player.level(), cameraEntity, camera.getPosition(), cameraEntity.getViewVector(client.getFrameTimeNs()));
            if (!player.getCooldowns().isOnCooldown(Items.SPYGLASS) && client.options.keyAttack.isDown()
                    && (Services.PLATFORM.hasSpyglassEnchant(player, ModRegistry.MARKING) || Services.PLATFORM.hasSpyglassEnchant(player, ModRegistry.SPOTTER))) {
                    Services.PLATFORM.useSpyglassEnch();
            }
        } else {
            DrawOverlay.hitResult = null;
        }
    }
}
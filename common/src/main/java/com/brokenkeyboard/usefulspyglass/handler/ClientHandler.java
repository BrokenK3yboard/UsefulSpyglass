package com.brokenkeyboard.usefulspyglass.handler;

import com.brokenkeyboard.usefulspyglass.DrawOverlay;
import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class ClientHandler {

    public static void handleClientTick(Minecraft client) {
        Player player = client.player;
        Camera camera = client.gameRenderer.getMainCamera();
        if (player != null && player.isScoping()) {
            DrawOverlay.hitResult = Services.PLATFORM.getHitResult(camera, client.getDeltaFrameTime(), player);
            if (!player.getCooldowns().isOnCooldown(Items.SPYGLASS) && client.options.keyAttack.isDown()
                    && (Services.PLATFORM.hasSpyglassEnchant(player, ModRegistry.MARKING) || Services.PLATFORM.hasSpyglassEnchant(player, ModRegistry.SPOTTER))) {
                Services.PLATFORM.useSpyglassEnch();
            }
        } else {
            DrawOverlay.hitResult = null;
        }
    }
}
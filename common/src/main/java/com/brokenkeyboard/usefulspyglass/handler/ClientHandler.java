package com.brokenkeyboard.usefulspyglass.handler;

import com.brokenkeyboard.usefulspyglass.EntityFinder;
import com.brokenkeyboard.usefulspyglass.InfoOverlay;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;

import static com.brokenkeyboard.usefulspyglass.InfoOverlay.hitResult;

public class ClientHandler {

    public static void handleClientTick(Minecraft client) {
        Player player = client.player;
        if (player != null && player.isScoping()) {
            InfoOverlay.setHitResult(EntityFinder.getAimedObject(client, 100));
            if (!player.getCooldowns().isOnCooldown(Items.SPYGLASS) && Services.PLATFORM.hasMarkingSpyglass(player) && hitResult instanceof EntityHitResult entityHit &&
                    entityHit.getEntity() instanceof LivingEntity entity && client.options.keyAttack.isDown()) {
                Services.PLATFORM.sendMarkingPacket(entity.getId(), entity.getLevel().dimension().location());
            }
        } else {
            InfoOverlay.setHitResult(null);
        }
    }
}
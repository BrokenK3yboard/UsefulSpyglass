package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.network.packet.MarkEntityPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import static com.brokenkeyboard.usefulspyglass.InfoOverlay.*;

public final class ClientEvents implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientEvents::onClientTick);
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            if (hitResult != null && ((hitResult instanceof EntityHitResult && ClientConfig.DISPLAY_ENTITIES.get()) ||
                    (hitResult instanceof BlockHitResult && ClientConfig.DISPLAY_BLOCKS.get()))) {
                DrawOverlay.drawGUI(graphics, hitResult, tooltipList, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
            }
        });
    }

    private static void onClientTick(Minecraft client) {
        Player player = client.player;
        if (player != null && player.isScoping()) {
            InfoOverlay.setHitResult(EntityFinder.getAimedObject(client, 100));
            ItemStack stack = player.getItemInHand(player.getUsedItemHand());
            if (EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.MARKING, stack) > 0 && hitResult instanceof EntityHitResult entity &&
                    entity.getEntity() instanceof LivingEntity living && !player.getCooldowns().isOnCooldown(stack.getItem()) && client.options.keyAttack.isDown()) {
                ClientPlayNetworking.send(new MarkEntityPacket(living.getId(), living.level().dimension().location()));
            }
        } else {
            InfoOverlay.setHitResult(null);
        }
    }
}
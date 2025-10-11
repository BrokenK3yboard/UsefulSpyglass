package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.brokenkeyboard.usefulspyglass.handler.ClientHandler;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.fml.config.ModConfig;

import static com.brokenkeyboard.usefulspyglass.DrawOverlay.hitResult;

public final class ClientEvents implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ForgeConfigRegistry.INSTANCE.register(ModRegistry.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ClientTickEvents.END_CLIENT_TICK.register(ClientHandler::handleClientTick);
        EntityRendererRegistry.register(ModRegistry.SPOTTER_EYE, context -> new ThrownItemRenderer<>(context, 1.0F, true));

        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            if ((!FabricLoader.getInstance().isModLoaded("jade") || !ClientConfig.JADE_INTEGRATION.get()) &&
                    ((hitResult instanceof EntityHitResult && ClientConfig.DISPLAY_ENTITIES.get()) || (hitResult instanceof BlockHitResult && ClientConfig.DISPLAY_BLOCKS.get()))) {
                DrawOverlay.drawGUI(graphics);
            }
        });
    }
}
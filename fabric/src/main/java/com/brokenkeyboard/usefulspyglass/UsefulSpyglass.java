package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.function.BiConsumer;

public class UsefulSpyglass implements ModInitializer {

    public static final ResourceLocation MARKING = new ResourceLocation(Constants.MOD_ID, "marking");

    @Override
    public void onInitialize() {
        ModLoadingContext.registerConfig(Constants.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ModLoadingContext.registerConfig(Constants.MOD_ID, ModConfig.Type.COMMON, CommonConfig.SPEC);
        ModRegistry.registerEnchantment(bind(Registry.ENCHANTMENT));

        ServerPlayNetworking.registerGlobalReceiver(MARKING, (server, player, handler, buf, responseSender) -> {
            int entityID = buf.readInt();
            ResourceLocation dimension = buf.readResourceLocation();
            ServerHandler.markEntity(player, entityID, dimension);
        });
    }

    private static <T> BiConsumer<ResourceLocation, T> bind(Registry<? super T> registry) {
        return (location, t) -> Registry.register(registry, location, t);
    }
}
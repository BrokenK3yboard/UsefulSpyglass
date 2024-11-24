package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.network.ServerHandler;
import com.brokenkeyboard.usefulspyglass.network.SpyglassEnchPayload;
import com.mojang.serialization.Codec;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.config.ModConfig;

@SuppressWarnings("UnstableApiUsage")
public class UsefulSpyglass implements ModInitializer {

    public static final AttachmentType<Double> PRECISION_BONUS = AttachmentRegistry.createPersistent(ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "precision_bonus"), Codec.DOUBLE);

    @Override
    public void onInitialize() {

        NeoForgeConfigRegistry.INSTANCE.register(ModRegistry.MOD_ID, ModConfig.Type.COMMON, CommonConfig.SPEC);

        Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "watcher_eye"), ModRegistry.SPOTTER_EYE);

        PayloadTypeRegistry.playC2S().register(SpyglassEnchPayload.TYPE, SpyglassEnchPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SpyglassEnchPayload.TYPE, (payload, context) -> context.server().execute(() ->
                ServerHandler.handleEnchantments(context.player())));
    }
}
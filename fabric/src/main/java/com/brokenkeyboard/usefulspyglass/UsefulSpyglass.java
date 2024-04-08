package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.handler.ServerHandler;
import com.brokenkeyboard.usefulspyglass.network.packet.MarkEntityPacket;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig;

import java.util.function.BiConsumer;

public class UsefulSpyglass implements ModInitializer {

    public static final PacketType<MarkEntityPacket> MARKING_TYPE = PacketType.create(new ResourceLocation(Constants.MOD_ID, "marking"), MarkEntityPacket::new);

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.COMMON, CommonConfig.SPEC);
        ModRegistry.registerEnchantment(bind(BuiltInRegistries.ENCHANTMENT));

        ServerPlayNetworking.registerGlobalReceiver(MARKING_TYPE.getId(), (server, player, handler, buf, responseSender) -> {
            int entityID = buf.readInt();
            ResourceLocation dimension = buf.readResourceLocation();
            ServerHandler.markEntity(player, entityID, dimension);
        });
    }

    private static <T> BiConsumer<ResourceLocation, T> bind(Registry<? super T> registry) {
        return (location, t) -> Registry.register(registry, location, t);
    }
}
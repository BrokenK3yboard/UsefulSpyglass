package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.enchantment.MarkingEnchantment;
import com.brokenkeyboard.usefulspyglass.network.packet.MarkEntityPacket;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.config.ModConfig;

public class UsefulSpyglass implements ModInitializer {

    public static final Enchantment MARKING = new MarkingEnchantment(EnchantmentCategory.BREAKABLE, EquipmentSlot.MAINHAND);
    public static final PacketType<MarkEntityPacket> MARKING_TYPE = PacketType.create(new ResourceLocation(Constants.MOD_ID, "marking"), MarkEntityPacket::new);

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(Constants.MOD_ID, "marking"), MARKING);
        ServerPlayNetworking.registerGlobalReceiver(MARKING_TYPE.getId(), MarkEntityPacket::receive);
        ForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
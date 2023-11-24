package com.brokenkeyboard.usefulspyglass.network.packet;

import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class MarkEntityPacket implements FabricPacket {

    private final int entityID;
    private final ResourceLocation dimension;

    public MarkEntityPacket(int entityID, ResourceLocation dimension) {
        this.entityID = entityID;
        this.dimension = dimension;
    }

    public MarkEntityPacket(FriendlyByteBuf buf) {
        entityID = buf.readInt();
        dimension = buf.readResourceLocation();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeResourceLocation(dimension);
    }

    @Override
    public PacketType<?> getType() {
        return UsefulSpyglass.MARKING_TYPE;
    }

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buf, PacketSender sender) {
        if(player != null) {
            int entityID = buf.readInt();
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
            LivingEntity entity = (LivingEntity) Objects.requireNonNull(server.getLevel(dimension)).getEntity(entityID);

            if(entity != null) {
                ItemStack stack = player.getItemInHand(player.getUsedItemHand());
                int level = EnchantmentHelper.getItemEnchantmentLevel(UsefulSpyglass.MARKING, stack);
                if (level > 0) {
                    entity.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.GLOWING, 80 + (40 * (level - 1)), 0)));
                }
            }
        }
    }
}
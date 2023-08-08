package com.brokenkeyboard.usefulspyglass.network.packet;

import com.brokenkeyboard.usefulspyglass.UsefulSpyglass;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MarkEntityPacket {

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

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeResourceLocation(dimension);
    }

    public static void handle(MarkEntityPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                LivingEntity entity = (LivingEntity) player.level().getEntity(packet.entityID);
                if(entity != null) {
                    if (entity.level().dimension().equals(ResourceKey.create(Registries.DIMENSION, packet.dimension))) {
                        ItemStack stack = player.getItemInHand(player.getUsedItemHand());
                        int level = EnchantmentHelper.getTagEnchantmentLevel(UsefulSpyglass.MARKING.get(), stack);
                        entity.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.GLOWING, 80 + (40 * level - 1), 0)));
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
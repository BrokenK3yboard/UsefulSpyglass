package com.brokenkeyboard.usefulspyglass.handler;

import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ServerHandler {

    public static void markEntity(ServerPlayer player, int entityID, ResourceLocation dimension) {
        if (player == null) return;
        LivingEntity entity = (LivingEntity) player.level.getEntity(entityID);
        ResourceKey<Level> levelKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimension);
        if (entity != null && entity.level.dimension().equals(levelKey) && !player.getCooldowns().isOnCooldown(Items.SPYGLASS) && Services.PLATFORM.hasMarkingSpyglass(player)) {
            entity.addEffect(new MobEffectInstance(new MobEffectInstance(MobEffects.GLOWING, CommonConfig.MARKING_DURATION.get())));
            player.getCooldowns().addCooldown(Items.SPYGLASS, (int) (CommonConfig.MARKING_DURATION.get() * 1.5));
        }
    }

    public static boolean usingPrecision(Player player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        boolean isCrossbow = stack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(stack);
        boolean isBow = CommonConfig.PRECISION_BOWS.get() && player.getUseItem().getItem() instanceof BowItem && (BowItem.getPowerForTime(player.getTicksUsingItem()) >= 1.0F);
        boolean hasSpyglass = !player.getCooldowns().isOnCooldown(Items.SPYGLASS) && Services.PLATFORM.hasPrecisionSpyglass(player);
        return (isBow || isCrossbow) && hasSpyglass && player.isCrouching();
    }

    public static float handlePrecision(Entity entity, Projectile projectile, float deviation) {
        if (entity instanceof Player player && !player.getCooldowns().isOnCooldown(Items.SPYGLASS) && player.isCrouching() && Services.PLATFORM.hasPrecisionSpyglass(player)) {
            projectile.setNoGravity(true);
            player.getCooldowns().addCooldown(Items.SPYGLASS, CommonConfig.PRECISION_COOLDOWN.get());
            return 0.0F;
        }
        return deviation;
    }
}
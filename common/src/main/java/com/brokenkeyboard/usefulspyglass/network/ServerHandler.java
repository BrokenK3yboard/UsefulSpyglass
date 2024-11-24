package com.brokenkeyboard.usefulspyglass.network;

import com.brokenkeyboard.usefulspyglass.EntityFinder;
import com.brokenkeyboard.usefulspyglass.ModRegistry;
import com.brokenkeyboard.usefulspyglass.SpotterEye;
import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.MarkedEntitiesAccess;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Predicate;

public class ServerHandler {

    private static final Predicate<Player> USING_PRECISION = player -> !player.getCooldowns().isOnCooldown(Items.SPYGLASS) && Services.PLATFORM.hasPrecision(player) && player.isCrouching();
    private static final Predicate<LivingEntity> BOW = (entity -> entity.getUseItem().getItem() instanceof BowItem && (BowItem.getPowerForTime(entity.getTicksUsingItem()) >= 1.0F));
    private static final Predicate<LivingEntity> CROSSBOW = (entity -> {
        ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
        return stack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(stack);
    });

    public static void handleEnchantments(ServerPlayer player) {
        if (player.getCooldowns().isOnCooldown(Items.SPYGLASS)) return;
        HitResult result = EntityFinder.getAimedObject(player.level(), player, player.getEyePosition(1.0F), player.getViewVector(1.0F).normalize());

        if (Services.PLATFORM.hasSpyglass(player, ModRegistry.MARKING) && result instanceof EntityHitResult entity) {
            ((MarkedEntitiesAccess) player).us$getMarkedEntities().addEntity(entity.getEntity(), CommonConfig.MARKING_DURATION.get());
            player.getCooldowns().addCooldown(Items.SPYGLASS, (int) (CommonConfig.MARKING_DURATION.get() * 0.8));
        } else if (Services.PLATFORM.hasSpyglass(player, ModRegistry.SPOTTER)) {
            SpotterEye eye = new SpotterEye(player.level(), player.getX(), player.getY(0.5), player.getZ(), player);
            eye.signalTo(new BlockPos((int) result.getLocation().x(), (int) result.getLocation().y() + 3, (int) result.getLocation().z()));
            player.level().gameEvent(GameEvent.PROJECTILE_SHOOT, eye.position(), GameEvent.Context.of(player));
            player.level().addFreshEntity(eye);
            player.getCooldowns().addCooldown(Items.SPYGLASS, (int) (CommonConfig.SPOTTER_DURATION.get() * 1.4));
        }
    }

    public static boolean usingPrecision(Player player) {
        return USING_PRECISION.test(player) && (BOW.test(player) || CROSSBOW.test(player) || Services.PLATFORM.testPrecisionCompat(player));
    }

    public static float handlePrecision(Entity entity, Projectile projectile, float deviation) {
        if (entity instanceof Player player && !player.getCooldowns().isOnCooldown(Items.SPYGLASS) && player.isCrouching() && Services.PLATFORM.hasPrecision(player)) {
            projectile.setNoGravity(true);
            Services.PLATFORM.setPrecisionBonus(projectile);
            player.getCooldowns().addCooldown(Items.SPYGLASS, CommonConfig.PRECISION_COOLDOWN.get());
            return 0.0F;
        }
        return deviation;
    }
}
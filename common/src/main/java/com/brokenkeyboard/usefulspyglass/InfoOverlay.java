package com.brokenkeyboard.usefulspyglass;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoOverlay {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    public static HitResult hitResult = null;
    public static List<TooltipInfo> tooltipList;
    public static int rectangleWidth;
    public static int rectangleHeight;
    public static int rectangleX;
    public static int rectangleY;

    private static void setComponent(List<TooltipInfo> list) {
        tooltipList = list;
        rectangleWidth = getLongest(tooltipList);
        rectangleHeight = 0;
        rectangleX = (int) ((CLIENT.getWindow().getGuiScaledWidth() * 0.5) - (rectangleWidth * 0.5));
        rectangleY = getPosition();
        for (TooltipInfo tooltip : tooltipList)
            rectangleHeight += tooltip.getHeight();
    }

    public static void setHitResult(HitResult result) {
        hitResult = result;
        if (hitResult == null) return;
        ArrayList<TooltipInfo> array = new ArrayList<>();
        if(hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity entity) {
            Component name = Component.literal(entity.getDisplayName().getString()).withStyle(getColor(entity));
            array.add(new TooltipInfo.TextTooltip(ClientTooltipComponent.create(name.getVisualOrderText())));
            Map<TooltipInfo.Icon, ClientTooltipComponent> map = new HashMap<>();
            map.put(TooltipInfo.Icon.HEALTH, ClientTooltipComponent.create(Component.literal(String.valueOf((int)entity.getHealth())).getVisualOrderText()));
            if (entity.getArmorValue() > 0) {
                map.put(TooltipInfo.Icon.ARMOR, ClientTooltipComponent.create(Component.literal(String.valueOf(entity.getArmorValue())).getVisualOrderText()));
            }
            array.add(new TooltipInfo.MobInfo(map));
            setComponent(array);
        } else if (result instanceof BlockHitResult blockHit) {
            BlockState state = CLIENT.player.level().getBlockState(blockHit.getBlockPos());
            array.add(new TooltipInfo.TextTooltip(ClientTooltipComponent.create(Component.literal(state.getBlock().getName().getString()).getVisualOrderText())));
            setComponent(array);
        }
    }

    public static int getLongest(List<TooltipInfo> list) {
        int result = 0;
        for(TooltipInfo tooltip : list)
            result = Math.max(tooltip.getWidth(), result);
        return result;
    }

    public static int getPosition() {
        return switch (ClientConfig.DISPLAY_POSITION.get()) {
            case 2 -> 10;
            case 1 -> (int) (CLIENT.getWindow().getGuiScaledHeight() * 0.5 + 10);
            default -> CLIENT.getWindow().getGuiScaledHeight() - 70;
        };
    }

    public static ChatFormatting getColor(LivingEntity entity) {
        if (entity instanceof Player) return ChatFormatting.BLUE;
        if (entity instanceof NeutralMob) return ChatFormatting.YELLOW;
        if (entity.getType().getCategory().isFriendly()) return ChatFormatting.GREEN;
        if (entity.getType().getCategory() == MobCategory.MONSTER) return ChatFormatting.RED;
        return ChatFormatting.WHITE;
    }
}
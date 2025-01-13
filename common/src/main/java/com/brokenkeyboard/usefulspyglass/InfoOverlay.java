package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
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
        int screenWidth = CLIENT.getWindow().getGuiScaledWidth();
        int screenHeight = CLIENT.getWindow().getGuiScaledHeight();

        for (TooltipInfo tooltip : tooltipList) {
            rectangleHeight += (tooltip.getHeight() + (hitResult instanceof BlockHitResult && tooltipList.size() == 1 ? 16 - tooltip.getHeight() : 0));
        }

        rectangleX = (int) ((screenWidth * ClientConfig.HUD_X.get()) - (rectangleWidth * 0.5));

        if (rectangleX - 4 < 0) {
            rectangleX = 4;
        } else if (rectangleX + rectangleWidth + 4 > screenWidth) {
            rectangleX = screenWidth - rectangleWidth - 4;
        }

        rectangleY = (int) (CLIENT.getWindow().getGuiScaledHeight() * ClientConfig.HUD_Y.get());

        if (rectangleY - 4 < 0) {
            rectangleY = 4;
        } else if (rectangleY + rectangleHeight + 2 > screenHeight) {
            rectangleY = screenHeight - rectangleHeight - 4;
        }
    }

    public static void setHitResult(HitResult result) {
        hitResult = result;
        if (hitResult == null) return;

        ArrayList<TooltipInfo> tooltipList = new ArrayList<>();

        if(hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity entity) {
            Component name = Component.literal(entity.getDisplayName().getString()).withStyle(getColor(entity));
            tooltipList.add(new TooltipInfo.TextTooltip(ClientTooltipComponent.create(name.getVisualOrderText())));

            Map<TooltipInfo.Icon, ClientTooltipComponent> map = new HashMap<>();
            map.put(TooltipInfo.Icon.HEALTH, ClientTooltipComponent.create(Component.literal(String.valueOf((int)entity.getHealth())).getVisualOrderText()));

            if (entity.getArmorValue() > 0) {
                map.put(TooltipInfo.Icon.ARMOR, ClientTooltipComponent.create(Component.literal(String.valueOf(entity.getArmorValue())).getVisualOrderText()));
            }

            tooltipList.add(new TooltipInfo.MobInfo(map));
            setComponent(tooltipList);
        } else if (hitResult instanceof BlockHitResult blockHit && CLIENT.player != null) {
            BlockState state = CLIENT.player.getLevel().getBlockState(blockHit.getBlockPos());
            String displayStr = state.getBlock().getName().getString();
            String[] arr = displayStr.split(" +");

            if(arr.length > 1) {
                int maxLength = (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 5);
                StringBuilder str = new StringBuilder();
                for (String addStr : arr) {
                    if (ClientTooltipComponent.create(Component.literal(str + " " + addStr).getVisualOrderText()).getWidth(CLIENT.font) > maxLength && !str.isEmpty()) {
                        tooltipList.add(new TooltipInfo.BlockInfo(ClientTooltipComponent.create(Component.literal(str.toString()).getVisualOrderText())));
                        str = new StringBuilder();
                    }
                    if(!str.isEmpty()) {
                        str.append(" ");
                    }
                    str.append(addStr);
                }
                tooltipList.add(new TooltipInfo.BlockInfo(ClientTooltipComponent.create(Component.literal(str.toString()).getVisualOrderText())));
            } else {
                tooltipList.add(new TooltipInfo.BlockInfo(ClientTooltipComponent.create(Component.literal(displayStr).getVisualOrderText())));
            }
            setComponent(tooltipList);
        }
    }

    public static int getLongest(List<TooltipInfo> list) {
        int result = 0;
        for(TooltipInfo tooltip : list) {
            result = Math.max(tooltip.getWidth(), result);
        }
        return result;
    }

    public static ChatFormatting getColor(LivingEntity entity) {
        if (entity instanceof Player) return ChatFormatting.BLUE;
        if (entity instanceof NeutralMob) return ChatFormatting.YELLOW;
        if (entity.getType().getCategory().isFriendly()) return ChatFormatting.GREEN;
        if (entity.getType().getCategory() == MobCategory.MONSTER) return ChatFormatting.RED;
        return ChatFormatting.WHITE;
    }
}
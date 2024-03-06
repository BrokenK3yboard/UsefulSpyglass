package com.brokenkeyboard.usefulspyglass;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.Map;

public class DrawOverlay {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    public static void drawGUI(GuiGraphics graphics, HitResult result, List<TooltipInfo> tooltipList, int rectX, int rectY, int rectW, int rectH) {
        graphics.pose().pushPose();

        TooltipRenderUtil.renderTooltipBackground(graphics, rectX, rectY, rectW, rectH, 400);

        if(result instanceof BlockHitResult blockHit && CLIENT.player != null) {
            BlockState state = CLIENT.player.level().getBlockState(blockHit.getBlockPos());
            ItemStack stack = state.getBlock().getCloneItemStack(CLIENT.player.level(), blockHit.getBlockPos(), state);
            renderStack(graphics, stack, null, rectX, rectY + rectH / 2 - 8);
        }

        int yOffset = rectY;
        for (TooltipInfo info : tooltipList) {
            if (info instanceof TooltipInfo.TextTooltip infoLine) {
                infoLine.TOOLTIP.renderText(CLIENT.font, rectX, yOffset, graphics.pose().last().pose(), graphics.bufferSource());
            } else if (info instanceof TooltipInfo.MobInfo infoLine) {
                int xOffset = rectX;
                for (Map.Entry<TooltipInfo.Icon, ClientTooltipComponent> entry : infoLine.MOB_INFO.entrySet()) {
                    if (entry.getKey() == TooltipInfo.Icon.HEALTH) {
                        renderIcon(graphics, xOffset, yOffset, TooltipInfo.Icon.HEALTH_EMPTY.ICON_X, TooltipInfo.Icon.HEALTH_EMPTY.ICON_Y);
                    }
                    renderIcon(graphics, xOffset, yOffset, entry.getKey().ICON_X, entry.getKey().ICON_Y);
                    xOffset += entry.getKey().ICON_WIDTH + 2;
                    entry.getValue().renderText(CLIENT.font, xOffset, yOffset + 1, graphics.pose().last().pose(), graphics.bufferSource());
                    xOffset += entry.getValue().getWidth(CLIENT.font) + 2;
                }
            } else if (info instanceof TooltipInfo.BlockInfo infoLine) {
                int offset = tooltipList.size() == 1 ? yOffset + (rectH / 2) - (CLIENT.font.lineHeight / 2) : yOffset;
                infoLine.TOOLTIP.renderText(CLIENT.font, rectX + 18, offset, graphics.pose().last().pose(), graphics.bufferSource());
            }
            yOffset += info.getHeight();
        }
        graphics.pose().popPose();
    }

    public static void renderIcon(GuiGraphics graphics, int x, int y, int iconX, int iconY) {
        graphics.pose().translate(0, 0, 400);
        graphics.pose().pushPose();
        graphics.blit(GUI_ICONS_LOCATION, x, y, iconX, iconY, 9, 9);
        graphics.pose().popPose();
    }

    public static void renderStack(GuiGraphics graphics, ItemStack stack, String countText, int x, int y) {
        graphics.pose().translate(0, 0, 400);
        graphics.pose().pushPose();
        Lighting.setupFor3DItems();
        RenderSystem.enableDepthTest();
        graphics.renderItem(stack, x, y);
        graphics.renderItemDecorations(CLIENT.font, stack, x, y, countText);
        Lighting.setupForFlatItems();
        RenderSystem.disableDepthTest();
        graphics.pose().popPose();
    }
}
package com.brokenkeyboard.usefulspyglass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class DrawOverlay {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    private static final int BACKGROUND_COLOR = -267386864;
    private static final int BORDER_COLOR_TOP = 1347420415;
    private static final int BORDER_COLOR_BOTTOM = 1344798847;

    public static void drawGUI(GuiGraphics graphics, HitResult result, List<TooltipInfo> tooltipList, int rectX, int rectY, int rectW, int rectH) {
        graphics.pose().pushPose();
        TooltipRenderUtil.renderTooltipBackground(graphics, rectX, rectY, rectW, rectH, 400, BACKGROUND_COLOR, BACKGROUND_COLOR, BORDER_COLOR_TOP, BORDER_COLOR_BOTTOM);
        int yOffset = rectY;

        if(result instanceof BlockHitResult)
            renderStack(graphics, ((TooltipInfo.BlockInfo)tooltipList.get(0)).getBlock(), null, rectX, rectY + rectH / 2 - 8);

        for(TooltipInfo info : tooltipList) {
            if (info instanceof TooltipInfo.MobInfo infoLine) {
                TooltipInfo.Icon icon = infoLine.getIcon();
                ClientTooltipComponent tooltip = infoLine.getTooltip();
                if (icon != TooltipInfo.Icon.NONE)
                    renderIcon(graphics, rectX, yOffset, icon.ICON_X, icon.ICON_Y);
                tooltip.renderText(CLIENT.font, rectX + icon.ICON_WIDTH + 2, yOffset, graphics.pose().last().pose(), graphics.bufferSource());
            } if (info instanceof TooltipInfo.BlockInfo blockInfo) {
                ClientTooltipComponent tooltip = blockInfo.getTooltip();
                if(tooltipList.size() == 1) {
                    tooltip.renderText(CLIENT.font, rectX + 18, yOffset + (rectH / 2) - (CLIENT.font.lineHeight / 2), graphics.pose().last().pose(), graphics.bufferSource());
                } else {
                    tooltip.renderText(CLIENT.font, rectX + 18, yOffset, graphics.pose().last().pose(), graphics.bufferSource());
                }
            }
            yOffset += info.getHeight();
        }
        graphics.pose().popPose();
    }

    public static void renderIcon(GuiGraphics graphics, int x, int y, int iconX, int iconY) {
        graphics.pose().translate(0, 0, 400);
        graphics.pose().pushPose();
        graphics.blit(GUI_ICONS_LOCATION, x, y, iconX, iconY,9, 9);
        graphics.pose().popPose();
    }

    public static void renderStack(GuiGraphics graphics, ItemStack stack, String countText, int x, int y) {
        graphics.pose().translate(0, 0, 400);
        graphics.pose().pushPose();
        graphics.renderItem(stack, x, y);
        graphics.renderItemDecorations(CLIENT.font, stack, x, y, countText);
        graphics.pose().popPose();
    }
}
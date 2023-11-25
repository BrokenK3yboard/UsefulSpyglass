package com.brokenkeyboard.usefulspyglass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class DrawOverlay {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    public static void drawGUI(GuiGraphics graphics, List<TooltipInfo> tooltipList, int rectX, int rectY, int rectW, int rectH) {
        graphics.pose().pushPose();

        TooltipRenderUtil.renderTooltipBackground(graphics, rectX, rectY, rectW, rectH, 400);

        int yOffset = rectY;
        for(TooltipInfo info : tooltipList) {
            if (info instanceof TooltipInfo.TextTooltip infoLine) {
                infoLine.TOOLTIP.renderText(CLIENT.font, rectX, yOffset, graphics.pose().last().pose(), graphics.bufferSource());
            } else if (info instanceof TooltipInfo.MobInfo infoLine) {
                int xOffset = rectX;
                for (Map.Entry<TooltipInfo.Icon, ClientTooltipComponent> entry : infoLine.MOB_INFO.entrySet()) {
                    if (entry.getKey() == TooltipInfo.Icon.HEALTH)
                        renderIcon(graphics,xOffset,yOffset, TooltipInfo.Icon.HEALTH_EMPTY.ICON_X, TooltipInfo.Icon.HEALTH_EMPTY.ICON_Y);
                    renderIcon(graphics, xOffset, yOffset, entry.getKey().ICON_X, entry.getKey().ICON_Y);
                    xOffset += entry.getKey().ICON_WIDTH + 2;
                    entry.getValue().renderText(CLIENT.font, xOffset, yOffset + 1, graphics.pose().last().pose(), graphics.bufferSource());
                    xOffset += entry.getValue().getWidth(CLIENT.font) + 2;
                }
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
}
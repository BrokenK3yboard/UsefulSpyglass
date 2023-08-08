package com.brokenkeyboard.usefulspyglass;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class DrawOverlay {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    protected static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    private static final int BACKGROUND_COLOR = -267386864;
    private static final int BORDER_COLOR_TOP = 1347420415;
    private static final int BORDER_COLOR_BOTTOM = 1344798847;

    public static void drawGUI(GuiGraphics graphics, HitResult result, List<TooltipInfo> tooltipList, int rectX, int rectY, int rectW, int rectH) {
        graphics.pose().pushPose();

        if(result instanceof BlockHitResult) {
            renderStack(graphics, ((TooltipInfo.BlockInfo)tooltipList.get(0)).getBlock(), null, rectX, rectY + rectH / 2 - 8);
        }

        TooltipRenderUtil.renderTooltipBackground(graphics, rectX, rectY, rectW, rectH, 400, BACKGROUND_COLOR, BACKGROUND_COLOR, BORDER_COLOR_TOP, BORDER_COLOR_BOTTOM);
        int yOffset = rectY;

        for(TooltipInfo info : tooltipList) {
            if (info instanceof TooltipInfo.MobInfo infoLine) {
                TooltipInfo.Icon icon = infoLine.getIcon();
                ClientTooltipComponent tooltip = infoLine.getTooltip();
                if (icon != TooltipInfo.Icon.NONE)
                    renderIcon(rectX, yOffset, icon.ICON_X, icon.ICON_Y, 8, 8, icon.ICON_WIDTH, icon.ICON_HEIGHT);
                tooltip.renderText(CLIENT.font, rectX + icon.ICON_WIDTH + 1, yOffset, graphics.pose().last().pose(), graphics.bufferSource());
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

    public static void renderIcon(int x, int y, int texX, int texY, int width, int height, int iconWidth, int iconHeight) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        float zLevel = 0.0F;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        RenderSystem.enableDepthTest();
        buffer.vertex(x, y + height, zLevel).uv(((float) (texX) * f), ((float) (texY + iconHeight) * f1)).endVertex();
        buffer.vertex(x + width, y + height, zLevel).uv( ((float) (texX + iconWidth) * f), ((float) (texY + iconHeight) * f1)).endVertex();
        buffer.vertex(x + width, y, zLevel).uv(((float) (texX + iconWidth) * f), ((float) (texY) * f1)).endVertex();
        buffer.vertex(x, y, zLevel).uv(((float) (texX) * f), ((float) (texY) * f1)).endVertex();
        RenderSystem.disableDepthTest();
        buffer.endVertex();
        tesselator.end();
    }

    public static void renderStack(GuiGraphics graphics, ItemStack stack, String countText, int x, int y) {
        Lighting.setupFor3DItems();
        RenderSystem.enableDepthTest();
        graphics.renderItem(stack, x, y);
        graphics.renderItemDecorations(CLIENT.font, stack, x, y, countText);
        Lighting.setupForFlatItems();
        RenderSystem.disableDepthTest();
    }
}
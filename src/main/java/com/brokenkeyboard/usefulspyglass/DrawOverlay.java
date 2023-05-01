package com.brokenkeyboard.usefulspyglass;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class DrawOverlay extends GuiComponent {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static void fillGradient(Matrix4f matrix, BufferBuilder buf, int x, int y, int w, int h, int start, int end) {
        fillGradient(matrix, buf, x, y, w, h, 400, start, end);
    }

    public static void drawGUI(PoseStack poseStack, HitResult result, List<InfoOverlay.TooltipInfo> tooltipList, int rectangleX, int rectangleY, int rectangleWidth, int rectangleHeight) {
        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();

        drawRect(matrix4f, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
        int yOffset = rectangleY;

        if(result instanceof BlockHitResult) {
            renderStack(((InfoOverlay.BlockInfo)tooltipList.get(0)).getBlock(), null, rectangleX, rectangleY + rectangleHeight / 2 - 8);
        }

        for(InfoOverlay.TooltipInfo info : tooltipList) {
            if (info instanceof InfoOverlay.MobInfo infoLine) {
                InfoOverlay.Icon icon = infoLine.getIcon();
                ClientTooltipComponent tooltip = infoLine.getTooltip();
                if (icon != InfoOverlay.Icon.NONE)
                    renderIcon(rectangleX, yOffset, icon.ICON_X, icon.ICON_Y, 8, 8, icon.ICON_WIDTH, icon.ICON_HEIGHT);
                renderText(matrix4f, tooltip, rectangleX + icon.ICON_WIDTH + 1, yOffset);
            } if (info instanceof InfoOverlay.BlockInfo blockInfo) {
                ClientTooltipComponent tooltip = blockInfo.getTooltip();
                if(tooltipList.size() == 1) {
                    renderText(matrix4f, tooltip, rectangleX + 18, yOffset + (rectangleHeight / 2) - (CLIENT.font.lineHeight / 2));
                } else {
                    renderText(matrix4f, tooltip, rectangleX + 18, yOffset);
                }
            }
            yOffset += info.getHeight();
        }
        poseStack.translate(0.0D, 0.0D, 400.0D);
        poseStack.popPose();
    }

    public static void renderText(Matrix4f matrix4f, ClientTooltipComponent component, int textX, int textY) {
        MultiBufferSource.BufferSource multiBufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        component.renderText(Minecraft.getInstance().font, textX, textY, matrix4f, multiBufferSource);
        multiBufferSource.endBatch();
    }

    public static void renderIcon(int x, int y, int texX, int texY, int width, int height, int iconWidth, int iconHeight) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        float zLevel = 0.0F;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);

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

    public static void renderStack(ItemStack stack, String countText, int x, int y) {
        Lighting.setupFor3DItems();
        RenderSystem.enableDepthTest();
        CLIENT.getItemRenderer().renderGuiItem(stack, x, y);
        CLIENT.getItemRenderer().renderGuiItemDecorations(CLIENT.font, stack, x, y, countText);
        Lighting.setupForFlatItems();
        RenderSystem.disableDepthTest();
    }

    public static void drawRect(Matrix4f matrix4f, int rectX, int rectY, int rectW, int rectH) {
        int rectGradient = -267386864;
        int borderGrad1 = 1347420415;
        int borderGrad2 = 1344798847;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // Top, bottom, center, left, right
        DrawOverlay.fillGradient(matrix4f, buffer, rectX - 3, rectY - 4, rectX + rectW + 3, rectY - 3, rectGradient, rectGradient);
        DrawOverlay.fillGradient(matrix4f, buffer, rectX - 3, rectY + rectH + 3, rectX + rectW + 3, rectY + rectH + 4, rectGradient, rectGradient);
        DrawOverlay.fillGradient(matrix4f, buffer, rectX - 3, rectY - 3, rectX + rectW + 3, rectY + rectH + 3, rectGradient, rectGradient);
        DrawOverlay.fillGradient(matrix4f, buffer, rectX - 4, rectY - 3, rectX - 3, rectY + rectH + 3, rectGradient, rectGradient);
        DrawOverlay.fillGradient(matrix4f, buffer, rectX + rectW + 3, rectY - 3, rectX + rectW + 4, rectY + rectH + 3, rectGradient, rectGradient);

        // Left, right, top, bottom
        DrawOverlay.fillGradient(matrix4f, buffer, rectX - 3, rectY - 3 + 1, rectX - 3 + 1, rectY + rectH + 3 - 1, borderGrad1, borderGrad2);
        DrawOverlay.fillGradient(matrix4f, buffer, rectX + rectW + 2, rectY - 3 + 1, rectX + rectW + 3, rectY + rectH + 3 - 1, borderGrad1, borderGrad2);
        DrawOverlay.fillGradient(matrix4f, buffer, rectX - 3, rectY - 3, rectX + rectW + 3, rectY - 3 + 1, borderGrad1, borderGrad1);
        DrawOverlay.fillGradient(matrix4f, buffer, rectX - 3, rectY + rectH + 2, rectX + rectW + 3, rectY + rectH + 3, borderGrad2, borderGrad2);
        buffer.endVertex();
        tesselator.end();
    }
}
package com.brokenkeyboard.usefulspyglass;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.List;

public class DrawOverlay extends GuiComponent {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static void fillGradient(Matrix4f matrix, BufferBuilder buf, int x, int y, int w, int h, int start, int end) {
        fillGradient(matrix, buf, x, y, w, h, 400, start, end);
    }

    public static void drawGUI(PoseStack poseStack, List<ClientTooltipComponent> clientTooltipList, int rectangleX, int rectangleY, int rectangleWidth, int rectangleHeight) {
        poseStack.pushPose();
        int height = CLIENT.font.lineHeight;

        Matrix4f matrix4f = poseStack.last().pose();

        drawRect(matrix4f /*, buffer*/, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
        DrawOverlay.renderIcon(rectangleX, rectangleY + height + (height) + 2, 52, 0, 8, 8, 9, 9);

        if(clientTooltipList.size() > 3) {
            DrawOverlay.renderIcon(rectangleX, rectangleY + height + (height * 2) + 3, 43, 9, 8, 8, 9, 9);
        }

        drawTooltip(matrix4f, clientTooltipList, rectangleX, rectangleY);
        poseStack.translate(0.0D, 0.0D, 400.0D);
        poseStack.popPose();
    }

    public static void drawTooltip(Matrix4f matrix4f, List<ClientTooltipComponent> tooltipList, int textX, int textY) {
        MultiBufferSource.BufferSource multiBufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        int textOffsetY = textY;

        for(int i2 = 0; i2 < tooltipList.size(); ++i2) {
            int textOffsetX = 0;

            if (i2 > 1) {
                textOffsetX += 10;
            }

            ClientTooltipComponent clientTooltipComponent = tooltipList.get(i2);
            clientTooltipComponent.renderText(Minecraft.getInstance().font, textX + textOffsetX, textOffsetY, matrix4f, multiBufferSource);
            textOffsetY += clientTooltipComponent.getHeight() /*+ (i2 == 0 ? 2 : 0)*/;
        }
        multiBufferSource.endBatch();
    }

    public static void drawRect(Matrix4f matrix4f/*,BufferBuilder buffer*/, int rectX, int rectY, int rectW, int rectH) {
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

        buffer.end();
        BufferUploader.reset();
    }

    public static int getLongest(List<ClientTooltipComponent> list) {
        int result = 0;
        for(ClientTooltipComponent tooltip : list) {
            int length = (list.indexOf(tooltip) > 0 ? 9 : 0) + tooltip.getWidth(Minecraft.getInstance().font);
            result = Math.max(length, result);
        }
        return result;
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
        tesselator.end();
    }
}
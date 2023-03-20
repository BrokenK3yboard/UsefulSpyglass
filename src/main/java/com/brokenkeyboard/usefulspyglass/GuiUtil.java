package com.brokenkeyboard.usefulspyglass;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.List;

public class GuiUtil extends GuiComponent {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static void fillGradient(Matrix4f matrix, BufferBuilder buf, int x, int y, int w, int h, int start, int end) {
        fillGradient(matrix, buf, x, y, w, h, 400, start, end);
    }

    public static void drawGUI(PoseStack poseStack, List<ClientTooltipComponent> clientTooltipList, int rectangleX, int rectangleY, int rectangleWidth, int rectangleHeight) {
        poseStack.pushPose();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Matrix4f matrix4f = poseStack.last().pose();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        drawRect(matrix4f, buffer, rectangleX, rectangleY, rectangleWidth, rectangleHeight);

        buffer.end();
        BufferUploader.end(buffer);

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

    public static void drawRect(Matrix4f matrix4f, BufferBuilder buf, int rectX, int rectY, int rectW, int rectH) {
        int rectGradient = -267386864;
        int borderGrad1 = 1347420415;
        int borderGrad2 = 1344798847;

        // Top, bottom, center, left, right
        GuiUtil.fillGradient(matrix4f, buf, rectX - 3, rectY - 4, rectX + rectW + 3, rectY - 3, rectGradient, rectGradient);
        GuiUtil.fillGradient(matrix4f, buf, rectX - 3, rectY + rectH + 3, rectX + rectW + 3, rectY + rectH + 4, rectGradient, rectGradient);
        GuiUtil.fillGradient(matrix4f, buf, rectX - 3, rectY - 3, rectX + rectW + 3, rectY + rectH + 3, rectGradient, rectGradient);
        GuiUtil.fillGradient(matrix4f, buf, rectX - 4, rectY - 3, rectX - 3, rectY + rectH + 3, rectGradient, rectGradient);
        GuiUtil.fillGradient(matrix4f, buf, rectX + rectW + 3, rectY - 3, rectX + rectW + 4, rectY + rectH + 3, rectGradient, rectGradient);

        // Left, right, top, bottom
        GuiUtil.fillGradient(matrix4f, buf, rectX - 3, rectY - 3 + 1, rectX - 3 + 1, rectY + rectH + 3 - 1, borderGrad1, borderGrad2);
        GuiUtil.fillGradient(matrix4f, buf, rectX + rectW + 2, rectY - 3 + 1, rectX + rectW + 3, rectY + rectH + 3 - 1, borderGrad1, borderGrad2);
        GuiUtil.fillGradient(matrix4f, buf, rectX - 3, rectY - 3, rectX + rectW + 3, rectY - 3 + 1, borderGrad1, borderGrad1);
        GuiUtil.fillGradient(matrix4f, buf, rectX - 3, rectY + rectH + 2, rectX + rectW + 3, rectY + rectH + 3, borderGrad2, borderGrad2);
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
        CLIENT.getTextureManager().bindForSetup(Gui.GUI_ICONS_LOCATION);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.enableDepthTest();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(x, y + height, zLevel).uv(((float) (texX) * f), ((float) (texY + iconHeight) * f1)).endVertex();
        buffer.vertex(x + width, y + height, zLevel).uv( ((float) (texX + iconWidth) * f), ((float) (texY + iconHeight) * f1)).endVertex();
        buffer.vertex(x + width, y, zLevel).uv(((float) (texX + iconWidth) * f), ((float) (texY) * f1)).endVertex();
        buffer.vertex(x, y, zLevel).uv(((float) (texX) * f), ((float) (texY) * f1)).endVertex();
        RenderSystem.disableDepthTest();
        tesselator.end();
    }
}
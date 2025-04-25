package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.platform.Services;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.Map;

public class DrawOverlay extends GuiComponent {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final boolean RENDER_BLOCK = Services.PLATFORM.shouldRenderBlock();

    public static void fillGradient(Matrix4f matrix, BufferBuilder buf, int x, int y, int w, int h, int start, int end) {
        fillGradient(matrix, buf, x, y, w, h, 400, start, end);
    }

    public static void drawGUI(PoseStack poseStack, HitResult result, List<TooltipInfo> tooltipList, int rectangleX, int rectangleY, int rectangleWidth, int rectangleHeight) {
        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();

        drawRect(poseStack, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
        poseStack.translate(0, 0, 400);
        int yOffset = rectangleY;

        if (RENDER_BLOCK && result instanceof BlockHitResult blockHit && CLIENT.player != null) {
            BlockState state = CLIENT.player.getLevel().getBlockState(blockHit.getBlockPos());
            ItemStack stack = state.getBlock().getCloneItemStack(CLIENT.player.getLevel(), blockHit.getBlockPos(), state);
            poseStack.pushPose();
            CLIENT.getItemRenderer().renderAndDecorateItem(stack, rectangleX, rectangleY + rectangleHeight / 2 - 8);
            poseStack.popPose();
        }

        for (TooltipInfo info : tooltipList) {
            if (info instanceof TooltipInfo.TextTooltip infoLine) {
                renderText(matrix4f, infoLine.TOOLTIP, rectangleX, yOffset);
            } else if (info instanceof TooltipInfo.MobInfo infoLine) {
                int xOffset = rectangleX;
                for (Map.Entry<TooltipInfo.Icon, ClientTooltipComponent> entry : infoLine.MOB_INFO.entrySet()) {
                    if (entry.getKey() == TooltipInfo.Icon.HEALTH) {
                        renderIcon(xOffset, yOffset, TooltipInfo.Icon.HEALTH_EMPTY.ICON_X, TooltipInfo.Icon.HEALTH_EMPTY.ICON_Y, 8,8, TooltipInfo.Icon.HEALTH.ICON_WIDTH, TooltipInfo.Icon.HEALTH.ICON_HEIGHT);
                    }
                    renderIcon(xOffset, yOffset, entry.getKey().ICON_X, entry.getKey().ICON_Y, 8, 8, entry.getKey().ICON_WIDTH, entry.getKey().ICON_HEIGHT);
                    xOffset += entry.getKey().ICON_WIDTH + 2;
                    renderText(matrix4f, entry.getValue(), xOffset, yOffset + 1);
                    xOffset += entry.getValue().getWidth(CLIENT.font) + 2;
                }
            } else if (info instanceof TooltipInfo.BlockInfo infoLine) {
                int offset = tooltipList.size() == 1 ? yOffset + (rectangleHeight / 2) - (CLIENT.font.lineHeight / 2) : yOffset;
                renderText(matrix4f, infoLine.TOOLTIP, rectangleX + (RENDER_BLOCK ? 18 : 1), offset);
            }
            yOffset += info.getHeight();
        }
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

    public static void drawRect(PoseStack poseStack, int rectX, int rectY, int rectW, int rectH) {
        int rectGradient = -267386864;
        int borderGrad1 = 1347420415;
        int borderGrad2 = 1344798847;

        poseStack.pushPose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = poseStack.last().pose();

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
        poseStack.popPose();
    }
}
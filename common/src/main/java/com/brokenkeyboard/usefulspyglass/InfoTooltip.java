package com.brokenkeyboard.usefulspyglass;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public abstract class InfoTooltip {

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    abstract int getWidth();
    abstract int getHeight();
    abstract void render(GuiGraphics graphics, int x, int y);

    static class Text extends InfoTooltip {
        public final ClientTooltipComponent TOOLTIP;

        public Text(ClientTooltipComponent tooltip) {
            this.TOOLTIP = tooltip;
        }

        @Override
        int getWidth() {
            return TOOLTIP.getWidth(Minecraft.getInstance().font);
        }

        @Override
        int getHeight() {
            return TOOLTIP.getHeight();
        }

        @Override
        void render(GuiGraphics graphics, int x, int y) {
            renderText(graphics, TOOLTIP, x, y);
        }
    }

    static class BlockName extends Text {

        public BlockName(ClientTooltipComponent tooltip) {
            super(tooltip);
        }

        @Override
        int getWidth() {
            return 18 + TOOLTIP.getWidth(Minecraft.getInstance().font);
        }

        @Override
        public int getHeight() {
            return 18;
        }

        @Override
        void render(GuiGraphics graphics, int x, int y) {
            if (DrawOverlay.hitResult instanceof BlockHitResult blockHit && Minecraft.getInstance().player != null) {
                BlockState state = Minecraft.getInstance().player.level().getBlockState(blockHit.getBlockPos());
                ItemStack stack = state.getBlock().getCloneItemStack(Minecraft.getInstance().player.level(), blockHit.getBlockPos(), state);
                renderStack(graphics, stack, x, y + 1);
            }
            renderText(graphics, TOOLTIP, x + 18, y + Minecraft.getInstance().font.lineHeight - (Minecraft.getInstance().font.lineHeight / 2));
        }
    }

    static class MobStatus extends InfoTooltip {
        public final Map<Icon, ClientTooltipComponent> MOB_INFO;

        public MobStatus(Map<Icon, ClientTooltipComponent> map) {
            this.MOB_INFO = map;
        }

        @Override
        public int getWidth() {
            int width = 0;
            for (Map.Entry<Icon, ClientTooltipComponent> entry : MOB_INFO.entrySet()) {
                width += (entry.getValue().getWidth(Minecraft.getInstance().font) + entry.getKey().ICON_WIDTH + 4);
            }
            return width;
        }

        @Override
        public int getHeight() {
            return Minecraft.getInstance().font.lineHeight + 1;
        }

        @Override
        void render(GuiGraphics graphics, int x, int y) {
            int xOffset = x;
            for (Map.Entry<InfoTooltip.Icon, ClientTooltipComponent> entry : MOB_INFO.entrySet()) {
                if (entry.getKey() == InfoTooltip.Icon.HEALTH) {
                    renderIcon(graphics, xOffset, y, entry.getKey().ICON_X, entry.getKey().ICON_Y);
                }
                renderIcon(graphics, xOffset, y, entry.getKey().ICON_X, entry.getKey().ICON_Y);
                xOffset += entry.getKey().ICON_WIDTH + 2;
                renderText(graphics, entry.getValue(), xOffset, y);
                xOffset += entry.getValue().getWidth(Minecraft.getInstance().font) + 2;
            }
        }
    }

    public enum Icon {
        HEALTH_EMPTY(16, 0, 9, 9),
        HEALTH(52, 0, 9, 9),
        ARMOR(43, 9, 9, 9);

        public final int ICON_X;
        public final int ICON_Y;
        public final int ICON_WIDTH;
        public final int ICON_HEIGHT;

        Icon(int iconX, int iconY, int iconWidth, int iconHeight) {
            this.ICON_X = iconX;
            this.ICON_Y = iconY;
            this.ICON_WIDTH = iconWidth;
            this.ICON_HEIGHT = iconHeight;
        }
    }

    private static void renderText(GuiGraphics graphics, ClientTooltipComponent component, int x, int y) {
        graphics.pose().translate(0, 0, 400);
        graphics.pose().pushPose();
        component.renderText(Minecraft.getInstance().font, x, y, graphics.pose().last().pose(), graphics.bufferSource());
        graphics.pose().popPose();
    }

    private static void renderIcon(GuiGraphics graphics, int x, int y, int iconX, int iconY) {
        graphics.pose().translate(0, 0, 400);
        graphics.pose().pushPose();
        graphics.blit(GUI_ICONS_LOCATION, x, y, iconX, iconY, 9, 9);
        graphics.pose().popPose();
    }

    private static void renderStack(GuiGraphics graphics, ItemStack stack, int x, int y) {
        graphics.pose().translate(0, 0, 400);
        graphics.pose().pushPose();
        Lighting.setupFor3DItems();
        RenderSystem.enableDepthTest();
        graphics.renderItem(stack, x, y);
        graphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y, null);
        Lighting.setupForFlatItems();
        RenderSystem.disableDepthTest();
        graphics.pose().popPose();
    }
}
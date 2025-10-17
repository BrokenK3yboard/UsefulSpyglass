package com.brokenkeyboard.usefulspyglass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public abstract class InfoTooltip {

    public static final ResourceLocation CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/container");
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
            return TOOLTIP.getHeight();
        }

        @Override
        void render(GuiGraphics graphics, int x, int y) {
            renderText(graphics, TOOLTIP, x + 18, y);
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
                    renderIcon(graphics, InfoTooltip.CONTAINER, xOffset, y);
                }
                renderIcon(graphics, entry.getKey().LOCATION, xOffset, y);
                xOffset += entry.getKey().ICON_WIDTH + 2;
                renderText(graphics, entry.getValue(), xOffset, y);
                xOffset += entry.getValue().getWidth(Minecraft.getInstance().font) + 2;
            }
        }
    }

    enum Icon {
        HEALTH(ResourceLocation.withDefaultNamespace("hud/heart/full"), 9, 9),
        ARMOR(ResourceLocation.withDefaultNamespace("hud/armor_full"),9, 9);

        public final ResourceLocation LOCATION;
        public final int ICON_WIDTH;
        public final int ICON_HEIGHT;

        Icon(ResourceLocation location, int iconWidth, int iconHeight) {
            this.LOCATION = location;
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

    private static void renderIcon(GuiGraphics graphics, ResourceLocation location, int x, int y) {
        graphics.pose().translate(0, 0, 400);
        graphics.pose().pushPose();
        graphics.blitSprite(location, x, y, 9, 9);
        graphics.pose().popPose();
    }
}
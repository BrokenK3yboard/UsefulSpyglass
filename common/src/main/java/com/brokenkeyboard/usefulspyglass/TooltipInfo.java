package com.brokenkeyboard.usefulspyglass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public abstract class TooltipInfo {

    public static final ResourceLocation CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/container");
    abstract int getWidth();
    abstract int getHeight();

    public static class TextTooltip extends TooltipInfo {
        public final ClientTooltipComponent TOOLTIP;

        public TextTooltip(ClientTooltipComponent tooltip) {
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
    }

    public static class MobInfo extends TooltipInfo {
        public final Map<Icon, ClientTooltipComponent> MOB_INFO;

        public MobInfo(Map<Icon, ClientTooltipComponent> map) {
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
            return Minecraft.getInstance().font.lineHeight;
        }
    }

    public static class BlockInfo extends TooltipInfo {
        public final ClientTooltipComponent TOOLTIP;

        public BlockInfo(ClientTooltipComponent tooltip) {
            this.TOOLTIP = tooltip;
        }

        @Override
        int getWidth() {
            return 18 + TOOLTIP.getWidth(Minecraft.getInstance().font);
        }

        @Override
        public int getHeight() {
            return Minecraft.getInstance().font.lineHeight;
        }
    }

    public enum Icon {
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
}
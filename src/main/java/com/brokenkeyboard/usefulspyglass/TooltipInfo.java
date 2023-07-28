package com.brokenkeyboard.usefulspyglass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public abstract class TooltipInfo {
    abstract int getWidth();
    abstract int getHeight();

    public static class MobInfo extends TooltipInfo {
        private final Icon icon;
        private final ClientTooltipComponent tooltip;

        public MobInfo(Icon icon, ClientTooltipComponent tooltip) {
            this.icon = icon;
            this.tooltip = tooltip;
        }

        public Icon getIcon() {
            return icon;
        }

        public ClientTooltipComponent getTooltip() {
            return tooltip;
        }

        @Override
        public int getWidth() {
            return icon.ICON_WIDTH + tooltip.getWidth(Minecraft.getInstance().font) + 1;
        }

        @Override
        public int getHeight() {
            return tooltip.getHeight();
        }
    }

    public static class BlockInfo extends TooltipInfo {
        private final ItemStack block;
        private final ClientTooltipComponent tooltip;

        public BlockInfo(ItemStack block, ClientTooltipComponent tooltip) {
            this.block = block;
            this.tooltip = tooltip;
        }

        public ItemStack getBlock() {
            return block;
        }

        public ClientTooltipComponent getTooltip() {
            return tooltip;
        }

        @Override
        int getWidth() {
            return 18 + tooltip.getWidth(Minecraft.getInstance().font);
        }

        @Override
        int getHeight() {
            return tooltip.getHeight();
        }
    }

    enum Icon {
        NONE(0, 0, 0, 0),
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
}
package com.brokenkeyboard.usefulspyglass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InfoOverlay {

    private static final ItemStack TOOLTIP_STACK = ItemStack.EMPTY;
    private static int healthStat;
    private static int armorStat;

    private static int rectangleWidth;
    private static int rectangleHeight;
    private static int rectangleX;
    private static int rectangleY;

    private static List<ClientTooltipComponent> clientTooltipList;

    public static final IIngameOverlay overlayBase = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.HUD_TEXT_ELEMENT, "hud_base", (gui, poseStack, partialTick, width, height) -> {
        gui.setupOverlayRenderState(true, false);
        Player player = Minecraft.getInstance().player;

        if(player != null && clientTooltipList != null && clientTooltipList.size() > 0) {
            GuiUtil.drawGUI(poseStack, clientTooltipList, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
        }
    });

    public static final IIngameOverlay statDisplay = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HUD_TEXT_ELEMENT, "hud_stats", (gui, poseStack, partialTick, width, height) -> {
        gui.setupOverlayRenderState(true, false);
        Player player = Minecraft.getInstance().player;

        if(player != null && clientTooltipList != null && clientTooltipList.size() > 0) {
            int heightOffset = clientTooltipList.get(0).getHeight();
            int yOffset = 1;

            if(healthStat > 0) {
                GuiUtil.renderIcon(rectangleX, rectangleY + heightOffset + heightOffset * yOffset, 52, 0, 8, 8, 9, 9);
            }
            if(armorStat > 0) {
                yOffset++;
                GuiUtil.renderIcon(rectangleX, rectangleY + heightOffset + heightOffset * yOffset, 43, 9, 8, 8, 9, 9);
            }
            //GuiUtil.renderIcon(rectangleX, rectangleY + clientTooltipList.get(0).getHeight() * yOffset + 2, 18, 95, 9, 9, 18, 18);
        }
    });

    public static void setMobComponent(Component name, Component relation, float health, int armor) {
        List<Component> tooltipList = new ArrayList<>();

        if(name != null) {
            healthStat = (int) health;
            armorStat = armor;

            tooltipList.add(name);
            tooltipList.add(relation);
            if (health > 0)
                tooltipList.add(new TextComponent("x " + (int)health));
            if (armor > 0)
                tooltipList.add(new TextComponent("x " + armor));
        }

        clientTooltipList = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponents(TOOLTIP_STACK, tooltipList, 0,
                Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(),
                Minecraft.getInstance().font, Minecraft.getInstance().font);

        // 75/427 hud/screen windowed, 85/480 fullscreen, or (int) (Minecraft.getInstance().getWindow().getGuiScaledWidth() * 0.18) - 6;
        rectangleWidth = GuiUtil.getLongest(clientTooltipList);
        rectangleHeight = 8 + (clientTooltipList.size() > 1 ? 2 : 0) + ((clientTooltipList.size() - 1) * 10);
        rectangleX = (int) ((Minecraft.getInstance().getWindow().getGuiScaledWidth() * 0.09) - (rectangleWidth * 0.5));
        rectangleY = (int) (Minecraft.getInstance().getWindow().getGuiScaledHeight() * 0.09);
    }
}
package com.brokenkeyboard.usefulspyglass;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = UsefulSpyglass.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InfoOverlay {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static HitResult hitResult = null;
    private static List<TooltipInfo> tooltipList;
    private static int rectangleWidth;
    private static int rectangleHeight;
    private static int rectangleX;
    private static int rectangleY;

    @SubscribeEvent
    public static void registerGUI(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "hud_base", (gui, poseStack, partialTick, width, height) -> {
            gui.setupOverlayRenderState(true, false);
            if (hitResult != null) {
                DrawOverlay.drawGUI(poseStack, hitResult, tooltipList, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
            }
        });
    }

    private static void setComponent(List<TooltipInfo> list) {
        // 75/427 hud/screen windowed, 85/480 fullscreen, or (int) (Minecraft.getInstance().getWindow().getGuiScaledWidth() * 0.18) - 6; 80 px fullscreen
        tooltipList = list;
        rectangleWidth = getLongest(tooltipList);
        rectangleHeight = 0;
        for (TooltipInfo tooltip : tooltipList)
            rectangleHeight += (tooltip.getHeight() + (hitResult instanceof BlockHitResult && tooltipList.size() == 1 ? 16 - tooltip.getHeight() : 0));
        rectangleX = (int) ((CLIENT.getWindow().getGuiScaledWidth() * 0.09) - (rectangleWidth * 0.5)) + 1;
        rectangleY = (int) (CLIENT.getWindow().getGuiScaledHeight() * 0.09);
    }

    public static void setHitResult(HitResult result) {
        hitResult = result;
        if (hitResult == null) return;
        ArrayList<TooltipInfo> array = new ArrayList<>();
        if(hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity entity) {
            Component mobRelation;

            if (entity instanceof NeutralMob || entity instanceof Player) {
                mobRelation = Component.translatable("text." + UsefulSpyglass.MOD_ID + ".neutral").withStyle(ChatFormatting.YELLOW);
            } else if (entity.getType().getCategory().isFriendly()) {
                mobRelation = Component.translatable("text." + UsefulSpyglass.MOD_ID + ".friendly").withStyle(ChatFormatting.GREEN);
            } else {
                mobRelation = Component.translatable("text." + UsefulSpyglass.MOD_ID + ".hostile").withStyle(ChatFormatting.RED);
            }

            array.add(new MobInfo(Icon.NONE, ClientTooltipComponent.create(entity.getDisplayName().getVisualOrderText())));
            array.add(new MobInfo(Icon.NONE, ClientTooltipComponent.create(mobRelation.getVisualOrderText())));
            array.add(new MobInfo(Icon.HEALTH, ClientTooltipComponent.create(Component.literal("x " + (int)entity.getHealth()).getVisualOrderText())));

            if (entity.getArmorValue() > 0)
                array.add(new MobInfo(Icon.ARMOR, ClientTooltipComponent.create(Component.literal("x " + entity.getArmorValue()).getVisualOrderText())));

            setComponent(array);
            hitResult = result;
        } else if (result instanceof BlockHitResult blockHit) {
            BlockState state = CLIENT.player.level.getBlockState(blockHit.getBlockPos());
            ItemStack stack = state.getBlock().getCloneItemStack(state, blockHit, CLIENT.player.level, blockHit.getBlockPos(), CLIENT.player);
            String displayStr = state.getBlock().getName().getString();
            String[] arr = displayStr.split("\s+");
            if(arr.length > 1) {
                int maxLength = (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 6) - 18;
                StringBuilder str = new StringBuilder();
                for (String addStr : arr) {
                    if (ClientTooltipComponent.create(Component.literal(str + " " + addStr).getVisualOrderText()).getWidth(CLIENT.font) > maxLength && str.length() > 0) {
                        array.add(new BlockInfo(stack, ClientTooltipComponent.create(Component.literal(str.toString()).getVisualOrderText())));
                        str = new StringBuilder();
                    }
                    if(str.length() > 0)
                        str.append(" ");
                    str.append(addStr);
                }
                array.add(new BlockInfo(stack, ClientTooltipComponent.create(Component.literal(str.toString()).getVisualOrderText())));
            } else {
                array.add(new BlockInfo(stack, ClientTooltipComponent.create(Component.literal(displayStr).getVisualOrderText())));
            }
            setComponent(array);
            hitResult = result;
        }
    }

    public static int getLongest(List<InfoOverlay.TooltipInfo> list) {
        int result = 0;
        for(InfoOverlay.TooltipInfo tooltip : list)
            result = Math.max(tooltip.getWidth(), result);
        return result;
    }

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
            return icon.ICON_WIDTH + tooltip.getWidth(Minecraft.getInstance().font);
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

    public abstract static class TooltipInfo {
        abstract int getWidth();
        abstract int getHeight();
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
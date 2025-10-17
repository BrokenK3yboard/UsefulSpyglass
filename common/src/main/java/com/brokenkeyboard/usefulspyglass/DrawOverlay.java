package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.brokenkeyboard.usefulspyglass.platform.Services;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DrawOverlay {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    public static HitResult hitResult = null;

    @SuppressWarnings("deprecation")
    public static void drawGUI(GuiGraphics graphics) {

        int screenWidth = CLIENT.getWindow().getGuiScaledWidth();
        int screenHeight = CLIENT.getWindow().getGuiScaledHeight();
        int xPos = (int) (screenWidth * ClientConfig.HUD_X.get());
        int yPos = (int) (screenHeight * ClientConfig.HUD_Y.get());
        ArrayList<InfoTooltip> tooltips = new ArrayList<>(), eventTooltips = new ArrayList<>();
        TooltipDimensions dimension = createTooltips(hitResult, tooltips, eventTooltips);

        boolean isBlock = DrawOverlay.hitResult instanceof BlockHitResult && Minecraft.getInstance().player != null;
        boolean oneLine = tooltips.size() == 1;
        int rectangleLeft = xPos - dimension.WIDTH / 2;
        int rectangleX = adjustAxis(rectangleLeft, dimension.WIDTH, screenWidth);
        int rectangleY = adjustAxis(yPos, dimension.BASE_HEIGHT + dimension.EXTRA_HEIGHT, screenHeight);
        int yOffset = isBlock && tooltips.size() == 1 ? rectangleY + Minecraft.getInstance().font.lineHeight - (Minecraft.getInstance().font.lineHeight / 2) : rectangleY;

        graphics.pose().pushPose();
        graphics.drawManaged(() -> TooltipRenderUtil.renderTooltipBackground(graphics, rectangleX, rectangleY, dimension.WIDTH, isBlock && tooltips.size() == 1 ? 18 : dimension.BASE_HEIGHT + dimension.EXTRA_HEIGHT, 400));

        if (isBlock) {
            BlockState state = Minecraft.getInstance().player.level().getBlockState(((BlockHitResult) hitResult).getBlockPos());
            ItemStack stack = state.getBlock().getCloneItemStack(Minecraft.getInstance().player.level(), ((BlockHitResult) hitResult).getBlockPos(), state);
            int stackYPos = oneLine ? rectangleY : rectangleY + dimension.BASE_HEIGHT / 2 - 8;
            renderStack(graphics, stack, rectangleLeft, stackYPos);
        }

        for (InfoTooltip info : tooltips) {
            info.render(graphics, xPos - dimension.X_OFFSET, yOffset);
            yOffset += info.getHeight();
        }

        for (InfoTooltip info : eventTooltips) {
            info.render(graphics, xPos - dimension.X_OFFSET_EXTRA, yOffset);
            yOffset += info.getHeight();
        }
        graphics.pose().popPose();
    }

    private static TooltipDimensions createTooltips(HitResult result, ArrayList<InfoTooltip> tooltips, ArrayList<InfoTooltip> eventTooltips) {
        MutableInt baseWidth = new MutableInt(0), baseHeight = new MutableInt(0), extraWidth = new MutableInt(0), extraHeight = new MutableInt(0);
        ArrayList<ClientTooltipComponent> toAdd = new ArrayList<>();

        if (result instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity entity) {
            String entityName = entity.getDisplayName().getString();

            Consumer<ClientTooltipComponent> textConsumer = tooltip -> {
                InfoTooltip.Text text = new InfoTooltip.Text(tooltip);
                tooltips.add(text);
                baseHeight.add(text.getHeight());
                baseWidth.setValue(Math.max(baseWidth.getValue(), text.getWidth()));
            };

            createTextLines(textConsumer, entityName, getEntityColor(entity));

            Map<InfoTooltip.Icon, ClientTooltipComponent> map = new HashMap<>();
            map.put(InfoTooltip.Icon.HEALTH, ClientTooltipComponent.create(Component.literal(String.valueOf((int) entity.getHealth())).getVisualOrderText()));

            if (entity.getArmorValue() > 0) {
                map.put(InfoTooltip.Icon.ARMOR, ClientTooltipComponent.create(Component.literal(String.valueOf(entity.getArmorValue())).getVisualOrderText()));
            }

            InfoTooltip.MobStatus status = new InfoTooltip.MobStatus(map);
            tooltips.add(status);
            baseHeight.add(status.getHeight());
            baseWidth.setValue(Math.max(baseWidth.getValue(), status.getWidth()));
            Services.PLATFORM.livingTooltipCallback(entity, toAdd);
        } else if (result instanceof BlockHitResult blockHit && CLIENT.player != null) {
            BlockPos pos = blockHit.getBlockPos();
            BlockState state = CLIENT.player.level().getBlockState(pos);
            String blockName = state.getBlock().getName().getString();

            Consumer<ClientTooltipComponent> textConsumer = tooltip -> {
                InfoTooltip.Text text = new InfoTooltip.BlockName(tooltip);
                tooltips.add(text);
                baseHeight.add(text.getHeight());
                baseWidth.setValue(Math.max(baseWidth.getValue(), text.getWidth()));
            };

            createTextLines(textConsumer, blockName, ChatFormatting.WHITE);
            Services.PLATFORM.blockTooltipCallback(state, pos, toAdd);
        }

        for (ClientTooltipComponent component : toAdd) {
            InfoTooltip.Text text = new InfoTooltip.Text(component);
            eventTooltips.add(text);
            extraHeight.add(text.getHeight());
            extraWidth.setValue(Math.max(extraWidth.getValue(), text.getWidth()));
        }

        int xOffset = baseWidth.getValue() / 2;
        int xOffsetExtra = baseWidth.getValue() > extraWidth.getValue() ? xOffset : extraWidth.getValue() / 2;
        int width = Math.max(baseWidth.getValue(), extraWidth.getValue());
        return new TooltipDimensions(xOffset, baseHeight.getValue(), xOffsetExtra, extraHeight.getValue(), width);
    }

    private static void createTextLines(Consumer<ClientTooltipComponent> consumer, String text, ChatFormatting color) {
        String[] strings = text.split(" +");

        if (strings.length > 1) {
            int maxLength = (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 5);
            StringBuilder str = new StringBuilder();

            for (String addStr : strings) {
                if (ClientTooltipComponent.create(Component.literal(str + " " + addStr).getVisualOrderText()).getWidth(CLIENT.font) > maxLength && !str.isEmpty()) {
                    consumer.accept(ClientTooltipComponent.create(Component.literal(str.toString()).getVisualOrderText()));
                    str = new StringBuilder();
                }

                if(!str.isEmpty()) {
                    str.append(" ");
                }

                str.append(addStr);
            }
            consumer.accept(ClientTooltipComponent.create(Component.literal(str.toString()).withStyle(color).getVisualOrderText()));
        } else {
            consumer.accept(ClientTooltipComponent.create(Component.literal(text).withStyle(color).getVisualOrderText()));
        }
    }

    private static int adjustAxis(int pos, int offset, int max) {
        if (pos - 4 < 0) {
            return 4;
        } else if (pos + offset + 4 > max) {
            return max - offset - 4;
        }
        return pos;
    }

    private static ChatFormatting getEntityColor(LivingEntity entity) {
        if (entity instanceof Player) return ChatFormatting.BLUE;
        if (entity instanceof NeutralMob) return ChatFormatting.YELLOW;
        if (entity.getType().getCategory().isFriendly()) return ChatFormatting.GREEN;
        if (entity.getType().getCategory() == MobCategory.MONSTER) return ChatFormatting.RED;
        return ChatFormatting.WHITE;
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

    private record TooltipDimensions(int X_OFFSET, int BASE_HEIGHT, int X_OFFSET_EXTRA, int EXTRA_HEIGHT, int WIDTH) { }
}
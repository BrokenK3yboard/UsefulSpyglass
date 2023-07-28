package com.brokenkeyboard.usefulspyglass;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Mod.EventBusSubscriber(modid = UsefulSpyglass.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InfoOverlay {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final ConcurrentMap<String, String> MOD_NAMES = getModList();
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
        int screenWidth = CLIENT.getWindow().getGuiScaledWidth();
        int screenHeight = CLIENT.getWindow().getGuiScaledHeight();

        for (TooltipInfo tooltip : tooltipList)
            rectangleHeight += (tooltip.getHeight() + (hitResult instanceof BlockHitResult && tooltipList.size() == 1 ? 16 - tooltip.getHeight() : 0));

        rectangleX = (int) ((screenWidth * ClientConfig.HUD_X.get()) - (rectangleWidth * 0.5));

        if (rectangleX - 4 < 0) {
            rectangleX = 4;
        } else if (rectangleX + rectangleWidth + 4 > screenWidth) {
            rectangleX = screenWidth - rectangleWidth - 4;
        }

        rectangleY = (int) (CLIENT.getWindow().getGuiScaledHeight() * ClientConfig.HUD_Y.get());

        if (rectangleY - 4 < 0) {
            rectangleY = 4;
        } else if (rectangleY + rectangleHeight + 2 > screenHeight) {
            rectangleY = screenHeight - rectangleHeight - 4;
        }
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

            array.add(new TooltipInfo.MobInfo(TooltipInfo.Icon.NONE, ClientTooltipComponent.create(entity.getDisplayName().getVisualOrderText())));
            array.add(new TooltipInfo.MobInfo(TooltipInfo.Icon.NONE, ClientTooltipComponent.create(mobRelation.getVisualOrderText())));
            array.add(new TooltipInfo.MobInfo(TooltipInfo.Icon.HEALTH, ClientTooltipComponent.create(Component.literal("x " + (int)entity.getHealth()).getVisualOrderText())));

            if (entity.getArmorValue() > 0)
                array.add(new TooltipInfo.MobInfo(TooltipInfo.Icon.ARMOR, ClientTooltipComponent.create(Component.literal("x " + entity.getArmorValue()).getVisualOrderText())));

            if (ClientConfig.DISPLAY_ENTITY_NAMEPSPACE.get()) {
                ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
                if (location != null && getModName(location.getNamespace()) != null) {
                    array.add(new TooltipInfo.MobInfo(TooltipInfo.Icon.NONE, ClientTooltipComponent.create(Component.literal(getModName(location.getNamespace())).
                            withStyle(ChatFormatting.BLUE).getVisualOrderText())));
                }
            }

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
                        array.add(new TooltipInfo.BlockInfo(stack, ClientTooltipComponent.create(Component.literal(str.toString()).getVisualOrderText())));
                        str = new StringBuilder();
                    }
                    if(str.length() > 0)
                        str.append(" ");
                    str.append(addStr);
                }
                array.add(new TooltipInfo.BlockInfo(stack, ClientTooltipComponent.create(Component.literal(str.toString()).getVisualOrderText())));
            } else {
                array.add(new TooltipInfo.BlockInfo(stack, ClientTooltipComponent.create(Component.literal(displayStr).getVisualOrderText())));
            }

            if (ClientConfig.DISPLAY_BLOCK_NAMEPSPACE.get()) {
                ResourceLocation location = ForgeRegistries.BLOCKS.getKey(state.getBlock());
                if (location != null && getModName(location.getNamespace()) != null) {
                    array.add(new TooltipInfo.BlockInfo(stack, ClientTooltipComponent.create(Component.literal(getModName(location.getNamespace())).
                            withStyle(ChatFormatting.BLUE).getVisualOrderText())));
                }
            }

            setComponent(array);
            hitResult = result;
        }
    }

    public static int getLongest(List<TooltipInfo> list) {
        int result = 0;
        for(TooltipInfo tooltip : list)
            result = Math.max(tooltip.getWidth(), result);
        return result;
    }

    public static String getModName(String namespace) {
        return MOD_NAMES.get(namespace);
    }

    public static ConcurrentMap<String, String> getModList() {
        ConcurrentMap<String, String> map = Maps.newConcurrentMap();
        List<IModInfo> list = ModList.get().getMods();
        for (IModInfo mod : list) {
            String modid = mod.getModId();
            String name = mod.getDisplayName() != null ? mod.getDisplayName() : modid;
            map.put(modid, name);
        }
        return map;
    }
}
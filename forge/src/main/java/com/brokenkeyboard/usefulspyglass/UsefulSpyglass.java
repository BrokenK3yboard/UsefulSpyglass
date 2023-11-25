package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.enchantment.MarkingEnchantment;
import com.brokenkeyboard.usefulspyglass.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.brokenkeyboard.usefulspyglass.InfoOverlay.*;

@Mod(Constants.MOD_ID)
public class UsefulSpyglass {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MOD_ID);
    public static final EnchantmentCategory SPYGLASS_ENCH = EnchantmentCategory.create("spyglass", item -> item instanceof SpyglassItem);
    public static final RegistryObject<Enchantment> MARKING = ENCHANTMENTS.register("marking", () -> new MarkingEnchantment(SPYGLASS_ENCH, EquipmentSlot.MAINHAND));

    public UsefulSpyglass() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ENCHANTMENTS.register(bus);
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void setup(final FMLCommonSetupEvent event) {
            event.enqueueWork(PacketHandler::register);
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Minecraft client = Minecraft.getInstance();
            Player player = client.player;
            if (event.phase == TickEvent.Phase.END && player != null && player.isScoping()) {
                HitResult result = EntityFinder.getAimedObject(client, 100);
                InfoOverlay.setHitResult(result);
                ItemStack stack = player.getItemInHand(player.getUsedItemHand());
                if (EnchantmentHelper.getTagEnchantmentLevel(UsefulSpyglass.MARKING.get(), stack) > 0 && result instanceof EntityHitResult entityHit &&
                        entityHit.getEntity() instanceof LivingEntity entity && !entity.isCurrentlyGlowing()) {
                    PacketHandler.sendPacket(entity, entity.level().dimension().location());
                }
            } else {
                InfoOverlay.setHitResult(null);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class OverlayEvent {

        @SubscribeEvent
        public static void registerGUI(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "hud_base", (gui, poseStack, partialTick, width, height) -> {gui.setupOverlayRenderState(true, false);
                if (hitResult != null && ((hitResult instanceof EntityHitResult && ClientConfig.DISPLAY_ENTITIES.get() ||
                        hitResult instanceof BlockHitResult && ClientConfig.DISPLAY_BLOCKS.get()))) {
                    DrawOverlay.drawGUI(poseStack, tooltipList, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
                }
            });
        }
    }
}
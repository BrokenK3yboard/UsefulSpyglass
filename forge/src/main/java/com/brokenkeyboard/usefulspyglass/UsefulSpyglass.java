package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.handler.ClientHandler;
import com.brokenkeyboard.usefulspyglass.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.brokenkeyboard.usefulspyglass.InfoOverlay.*;

@Mod(Constants.MOD_ID)
public class UsefulSpyglass {

    public UsefulSpyglass() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        register(ForgeRegistries.ENCHANTMENTS.getRegistryKey(), ModRegistry::registerEnchantment);
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void setup(final FMLCommonSetupEvent event) {
            event.enqueueWork(PacketHandler::register);
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents {

        @SubscribeEvent
        public static void registerGUI(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "hud_base", (gui, poseStack, partialTick, width, height) -> {gui.setupOverlayRenderState(true, false);
                if (hitResult != null && ((hitResult instanceof EntityHitResult && ClientConfig.DISPLAY_ENTITIES.get()) ||
                        (hitResult instanceof BlockHitResult && ClientConfig.DISPLAY_BLOCKS.get()))) {
                    DrawOverlay.drawGUI(poseStack, hitResult, tooltipList, rectangleX, rectangleY, rectangleWidth, rectangleHeight);
                }
            });
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            ClientHandler.handleClientTick(Minecraft.getInstance());
        }
    }

    public static <T> void register(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<ResourceLocation, T>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) ->
                source.accept(((location, t) -> event.register(registry, location, () -> t))));
    }
}
package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.handler.ClientHandler;
import com.brokenkeyboard.usefulspyglass.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.brokenkeyboard.usefulspyglass.DrawOverlay.hitResult;

@Mod(ModRegistry.MOD_ID)
public class UsefulSpyglass {

    public static final ArrayList<Predicate<?>> PRECISION_COMPATIBLE = new ArrayList<>();
    public static final String IMC_ID = "precision";

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ModRegistry.MOD_ID);
    public static final Supplier<EntityType<?>> WATCHER_EYE = ENTITIES.register("watcher_eye", () -> ModRegistry.SPOTTER_EYE);

    public UsefulSpyglass(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        ENTITIES.register(bus);
        register(context, Registries.ENCHANTMENT, ModRegistry::registerEnchantment);
        bus.addListener(this::imcProcess);
    }

    public void imcProcess(final InterModProcessEvent event) {
        event.getIMCStream().forEach(imcMessage -> {
            if (imcMessage.method().equals(IMC_ID) && imcMessage.messageSupplier().get() instanceof Predicate<?> predicate) {
                PRECISION_COMPATIBLE.add(predicate);
            }
        });
    }

    @Mod.EventBusSubscriber(modid = ModRegistry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void setup(final FMLCommonSetupEvent event) {
            event.enqueueWork(PacketHandler::register);
        }
    }

    @Mod.EventBusSubscriber(modid = ModRegistry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistryEvents {

        @SubscribeEvent
        public static void registerGUI(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "hud_base", (gui, graphics, partialTick, width, height) -> {gui.setupOverlayRenderState(true, false);
                if ((!ModList.get().isLoaded("jade") || !ClientConfig.JADE_INTEGRATION.get()) &&
                        ((hitResult instanceof EntityHitResult && ClientConfig.DISPLAY_ENTITIES.get()) || (hitResult instanceof BlockHitResult && ClientConfig.DISPLAY_BLOCKS.get()))) {
                    DrawOverlay.drawGUI(graphics);
                }
            });
        }
    }

    @Mod.EventBusSubscriber(modid = ModRegistry.MOD_ID)
    public static class Events {

        @SubscribeEvent
        public static void livingDamage(LivingDamageEvent event) {
            if (event.getSource().is(DamageTypeTags.IS_PROJECTILE) && event.getSource().getDirectEntity() instanceof Projectile projectile &&
                    projectile.getTags().contains("precision")) {
                event.setAmount(event.getAmount() * 1.2F);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ModRegistry.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            ClientHandler.handleClientTick(Minecraft.getInstance());
        }
    }

    public static <T> void register(FMLJavaModLoadingContext context, ResourceKey<Registry<T>> registry, Consumer<BiConsumer<ResourceLocation, T>> source) {
        context.getModEventBus().addListener((RegisterEvent event) ->
                source.accept(((location, supplier) -> event.register(registry, location, () -> supplier))));
    }
}
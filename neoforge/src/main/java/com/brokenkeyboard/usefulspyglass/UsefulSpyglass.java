package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.config.ClientConfig;
import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.brokenkeyboard.usefulspyglass.network.ClientHandler;
import com.brokenkeyboard.usefulspyglass.network.ServerHandler;
import com.brokenkeyboard.usefulspyglass.network.SpyglassEnchPayload;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.brokenkeyboard.usefulspyglass.DrawOverlay.hitResult;

@Mod(ModRegistry.MOD_ID)
public class UsefulSpyglass {

    public static final ArrayList<Predicate<?>> PRECISION_COMPATIBLE = new ArrayList<>();
    public static final String IMC_ID = "precision";

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ModRegistry.MOD_ID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ModRegistry.MOD_ID);
    public static final Supplier<AttachmentType<Double>> PRECISION_BONUS = ATTACHMENTS.register("precision_bonus", () -> AttachmentType.builder(() -> 1D).serialize(Codec.DOUBLE).build());
    public static final Supplier<EntityType<?>> WATCHER_EYE = ENTITIES.register("watcher_eye", () -> ModRegistry.SPOTTER_EYE);

    public UsefulSpyglass(ModContainer container, IEventBus bus) {
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        ENTITIES.register(bus);
        ATTACHMENTS.register(bus);
        bus.addListener(this::imcProcess);
    }

    private void imcProcess(final InterModProcessEvent event) {
        event.getIMCStream().forEach(imcMessage -> {
            if (imcMessage.method().equals(IMC_ID) && imcMessage.messageSupplier().get() instanceof Predicate<?> predicate) {
                PRECISION_COMPATIBLE.add(predicate);
            }
        });
    }

    @EventBusSubscriber(modid = ModRegistry.MOD_ID)
    public static class Events {

        @SubscribeEvent
        public static void register(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");

            registrar.playToServer(SpyglassEnchPayload.TYPE, SpyglassEnchPayload.STREAM_CODEC, (data, context) -> context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer serverPlayer) {
                    ServerHandler.handleEnchantments(serverPlayer);
                }
            }));
        }

        @SubscribeEvent
        public static void damageEvent(LivingIncomingDamageEvent event) {
            if (event.getSource().is(DamageTypeTags.IS_PROJECTILE) && event.getSource().getDirectEntity() instanceof Projectile projectile &&
                    projectile.hasData(PRECISION_BONUS)) {
                event.setAmount((float) (event.getAmount() * projectile.getData(PRECISION_BONUS)));
            }
        }
    }

    @EventBusSubscriber(modid = ModRegistry.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void registerGUI(RegisterGuiLayersEvent event) {
            event.registerAbove(VanillaGuiLayers.DEBUG_OVERLAY, ResourceLocation.fromNamespaceAndPath(ModRegistry.MOD_ID, "hud_base"), (gui, deltaTracker) -> {
                if ((!ModList.get().isLoaded("jade") || !ClientConfig.JADE_INTEGRATION.get()) &&
                        ((hitResult instanceof EntityHitResult && ClientConfig.DISPLAY_ENTITIES.get()) || (hitResult instanceof BlockHitResult && ClientConfig.DISPLAY_BLOCKS.get()))) {
                    DrawOverlay.drawGUI(gui);
                }
            });
        }

        @SubscribeEvent
        public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModRegistry.SPOTTER_EYE, context -> new ThrownItemRenderer<>(context, 1.0F, true));
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            ClientHandler.handleClientTick(Minecraft.getInstance());
        }
    }
}
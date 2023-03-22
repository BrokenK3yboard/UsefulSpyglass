package com.brokenkeyboard.usefulspyglass;

import com.brokenkeyboard.usefulspyglass.enchantment.MarkingEnchantment;
import com.brokenkeyboard.usefulspyglass.network.PacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(UsefulSpyglass.MOD_ID)
public class UsefulSpyglass
{
    public static final String MOD_ID = "usefulspyglass";

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MOD_ID);
    public static final EnchantmentCategory SPYGLASS_ENCH = EnchantmentCategory.create("spyglass", item -> item instanceof SpyglassItem);
    public static final RegistryObject<Enchantment> MARKING = ENCHANTMENTS.register("marking", () -> new MarkingEnchantment(
            Enchantment.Rarity.COMMON, SPYGLASS_ENCH, EquipmentSlot.MAINHAND));
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static final RegistryObject<Item> SPYGLASS = ITEMS.register("spyglass", () -> new
            com.brokenkeyboard.usefulspyglass.SpyglassItem((new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)));

    public UsefulSpyglass() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(InfoOverlay::registerGUI);
        ITEMS.register(bus);
        ENCHANTMENTS.register(bus);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void setup(final FMLClientSetupEvent event) {
            event.enqueueWork(PacketHandler::register);
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Minecraft client = Minecraft.getInstance();
            Player player = client.player;
            if (event.phase == TickEvent.Phase.END && player != null) {
                ItemStack stack = player.getItemInHand(player.getUsedItemHand());
                if (stack.getItem() instanceof SpyglassItem && player.isUsingItem()) {
                    HitResult hitResult = EntityFinder.getAimedEntity(client, 100);
                    if(hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity entity) {
                        Component relation;

                        if (entity instanceof NeutralMob || entity instanceof Player) {
                            relation = Component.translatable("text." + UsefulSpyglass.MOD_ID + ".neutral").withStyle(ChatFormatting.YELLOW);
                        } else if (entity.getType().getCategory().isFriendly()) {
                            relation = Component.translatable("text." + UsefulSpyglass.MOD_ID + ".friendly").withStyle(ChatFormatting.GREEN);
                        } else {
                            relation = Component.translatable("text." + UsefulSpyglass.MOD_ID + ".hostile").withStyle(ChatFormatting.RED);
                        }

                        InfoOverlay.setMobComponent(entity.getName(), relation, entity.getHealth(), entity.getArmorValue());
                        if (EnchantmentHelper.getItemEnchantmentLevel(UsefulSpyglass.MARKING.get(), stack) > 0 && !entity.isCurrentlyGlowing()) {
                            PacketHandler.sendPacket(entity, entity.getLevel().dimension().location());
                        }
                    } else {
                        InfoOverlay.setMobComponent(null, null,0, 0);
                    }
                } else {
                    InfoOverlay.setMobComponent(null, null,0, 0);
                }
            }
        }
    }
}
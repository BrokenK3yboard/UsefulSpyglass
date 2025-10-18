# Useful Spyglass

This mod gives the spyglass actual utility so it is no longer "optifine zoom in item form".<br>
Mobs viewed through a spyglass will display their name, relation, health, and armor.<br>
Blocks viewed through a spyglass will have their name displayed.<br>

This mod also adds spyglass enchantments. Usage of any enchantment will put the spyglass on cooldown, preventing further enchantment use.
* Marking: Pressing the attack keybind while viewing a mob will cause it to glow for a period of time. The glowing effect is only visible to the user.
* Precision: Offhanding a spyglass and crouching while using a ranged weapon grants zoom. Projectiles gain perfect accuracy and deal extra damage.  
* Spotter: Pressing the attack keybind while looking through a spyglass summons an eye of ender to constantly mark entities near the targeted position.

## Integration

* If Jade is installed, its overlay will be shown instead of the builtin one. This is configurable.
* If Spyglass improvements is installed, enchantments can be used if the spyglass is placed in the curio/trinkets/accessories slot.
* If Enchantment descriptions or other equivalent mod is installed, the details of each enchantment will be displayed ingame.

The Precision enchantment can be made compatible with ranged weapons from other mods from version 0.7.0 onwards, provided they extend ```ProjectileWeaponItem```.<br>
Mods that add custom bows and crossbows that extend ```ProjectileWeaponItem``` should be supported by default.<br>
Below are examples that mod authors can use to make their weapons compatible:<br>

First, define a ```Predicate<LivingEntity>``` indicating when the zooming behavior should be allowed. This step is the same regardless of modloader.<br>

```
private static final Predicate<LivingEntity> CROSSBOW = (entity -> {
    ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
    return stack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(stack);
});
```

### Forge/Neoforge:

Inside an InterModQueueEvent, add the following:

```InterModComms.sendTo("usefulspyglass", "precision", () -> CROSSBOW);```

The first two arguments should be the same as above, the last should be the predicate.

### Fabric:

Inside the mod's onInitialize() function, add the following:

```FabricLoader.getInstance().getObjectShare().put("usefulspyglass:" + Items.CROSSBOW, CROSSBOW);```

The first string should always be "usefulspyglass:" followed by the item, and the second should be the predicate.

---

This mod also provides event handlers that can be used by other mods to add extra information to mob and block tooltips.<br>
Below are examples that mod authors can use:

### Forge:

```
@EventBusSubscriber(modid = ModRegistry.MOD_ID, value = Dist.CLIENT)
public static class ClientEvents {

    @SubscribeEvent
    public static void onLivingTooltip(LivingTooltipEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.getTooltipList().add(ClientTooltipComponent.create(Component.literal("AW MAN").getVisualOrderText()));
        }
    }

    @SubscribeEvent
    public static void onBlockTooltip(BlockTooltipEvent event) {
        if (event.getBlockState().is(Blocks.DIRT)) {
            event.getTooltipList().add(ClientTooltipComponent.create(Component.literal("DIAMONDS").getVisualOrderText()));
        }
    }
}
```

### Fabric:
```
@Override
public void onInitializeClient() {

    LivingTooltipCallback.EVENT.register((entity, tooltipInfoList) -> {
        if (entity instanceof Creeper) {
            tooltipInfoList.add(ClientTooltipComponent.create(Component.literal("AW MAN").getVisualOrderText()));
        }
    });

    BlockTooltipCallback.EVENT.register((state, pos, tooltipInfoList) -> {
        if (state.is(Blocks.DIRT)) {
            tooltipInfoList.add(ClientTooltipComponent.create(Component.literal("DIAMONDS").getVisualOrderText()));
        }
    });
}
```
Note that both events are done on the client side.

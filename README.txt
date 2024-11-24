This mod gives the spyglass actual utility so it is no longer "optifine zoom in item form".

Mobs viewed through a spyglass will display their name, relation, health, and armor.
Blocks viewed through a spyglass will have their name displayed.

Marking Enchantment: Pressing the attack keybind marks mobs viewed through a spyglass.
Precision Enchantment: Offhanding a spyglass and crouching while using a ranged weapon grants zoom. Projectiles gain perfect accuracy and deal extra damage.
Spotter Enchantment: Pressing the attack keybind while looking through a spyglass summons an eye of ender to constantly mark entities near the targeted position.

Precision can be made compatible with ranged weapons from other mods from version 0.7.0 onwards, provided they extend ProjectileWeaponItem.
Mods that add custom bows and crossbows that extend ProjectileWeaponItem should be supported by default.

Below are examples that mod authors can use to make their weapons compatible:

Neoforge:

1. Define a Predicate<LivingEntity> indicating when the zooming behavior should be allowed:

private static final Predicate<LivingEntity> CROSSBOW = (entity -> {
	ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
	return stack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(stack);
});

2. Inside an InterModQueueEvent, add the following:

InterModComms.sendTo("usefulspyglass", "precision", () -> CROSSBOW);

The first two arguments should be the same as above, the last should be the predicate.

Fabric:

1. Define a predicate as shown above.

2. Inside the mod's onInitialize() function, add the following:

FabricLoader.getInstance().getObjectShare().put("usefulspyglass:" + Items.CROSSBOW, CROSSBOW);

The first string should always be "usefulspyglass:" followed by the item, and the second should be the predicate.
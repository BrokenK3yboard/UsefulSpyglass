package com.brokenkeyboard.usefulspyglass;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class Constants {

	public static final String MOD_ID = "usefulspyglass";
	public static final String MOD_NAME = "Useful Spyglass";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final Predicate<ItemStack> PRECISION_SPYGLASS = stack -> stack.getItem() == Items.SPYGLASS && EnchantmentHelper.getEnchantments(stack).containsKey(ModRegistry.PRECISION);
}
package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.enchantment.MarkingEnchantment;
import com.brokenkeyboard.usefulspyglass.enchantment.PrecisionEnchantment;
import com.brokenkeyboard.usefulspyglass.enchantment.SpotterEnchantment;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @WrapOperation(method = "getAvailableEnchantmentResults", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean getEnchants(EnchantmentCategory instance, Item item, Operation<Boolean> operation, int i, ItemStack stack, boolean bl, @Local(ordinal = 0) Enchantment enchantment) {
        if (enchantment instanceof MarkingEnchantment || enchantment instanceof PrecisionEnchantment || enchantment instanceof SpotterEnchantment) {
            return stack.getItem() instanceof SpyglassItem;
        }
        return operation.call(instance, item);
    }
}
package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.enchantment.MarkingEnchantment;
import com.brokenkeyboard.usefulspyglass.enchantment.PrecisionEnchantment;
import com.brokenkeyboard.usefulspyglass.enchantment.SpotterEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = "canEnchant", at = @At(value = "HEAD"), cancellable = true)
    private void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Enchantment enchantment = ((Enchantment) (Object) this);
        if (enchantment instanceof MarkingEnchantment || enchantment instanceof PrecisionEnchantment || enchantment instanceof SpotterEnchantment) {
            cir.setReturnValue(stack.getItem() instanceof SpyglassItem);
        }
    }
}
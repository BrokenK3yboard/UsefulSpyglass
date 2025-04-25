package com.brokenkeyboard.usefulspyglass.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpyglassItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract Item asItem();

    @Inject(method = "isEnchantable", at = @At(value = "RETURN"), cancellable = true)
    protected void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        if ((this).asItem() instanceof SpyglassItem) cir.setReturnValue(true);
    }

    @Inject(method = "getEnchantmentValue", at = @At(value = "RETURN"), cancellable = true)
    protected void getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        if ((this).asItem() instanceof SpyglassItem) cir.setReturnValue(1);
    }
}
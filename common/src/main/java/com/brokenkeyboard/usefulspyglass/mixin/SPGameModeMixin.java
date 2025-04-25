package com.brokenkeyboard.usefulspyglass.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class SPGameModeMixin {

    @Inject(method = "useItem", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void useItem(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        if (stack.getItem() instanceof SpyglassItem) {
            int amount = stack.getCount();
            int damage = stack.getDamageValue();
            InteractionResultHolder<ItemStack> result = stack.use(level, player, hand);
            ItemStack stack1 = result.getObject();
            if (stack1 == stack && stack1.getCount() == amount && stack1.getUseDuration() <= 0 && stack1.getDamageValue() == damage) {
                cir.setReturnValue(result.getResult());
            } else if (result.getResult() == InteractionResult.FAIL && stack1.getUseDuration() > 0 && !player.isUsingItem()) {
                cir.setReturnValue(result.getResult());
            } else {
                if (stack != stack1) {
                    player.setItemInHand(hand, stack1);
                }

                if (((ServerPlayerGameMode) (Object)this).isCreative() && stack1 != ItemStack.EMPTY) {
                    stack1.setCount(amount);
                    if (stack1.isDamageableItem() && stack1.getDamageValue() != damage) {
                        stack1.setDamageValue(damage);
                    }
                }

                if (stack1.isEmpty()) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                }

                if (!player.isUsingItem()) {
                    player.inventoryMenu.sendAllDataToRemote();
                }
                cir.setReturnValue(result.getResult());
            }
        }
    }
}
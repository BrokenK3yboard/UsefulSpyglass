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
            InteractionResultHolder<ItemStack> result = stack.use(level, player, hand);
            ItemStack stack1 = result.getObject();

            if (stack != stack1) {
                player.setItemInHand(hand, stack1);
            }

            if (!player.isUsingItem()) {
                player.inventoryMenu.sendAllDataToRemote();
            }
            cir.setReturnValue(result.getResult());
        }
    }
}
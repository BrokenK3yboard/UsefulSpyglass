package com.brokenkeyboard.usefulspyglass.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MPGameModeMixin {

    @Inject(method = "method_41929", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/mutable/MutableObject;setValue(Ljava/lang/Object;)V",
            shift = At.Shift.AFTER, ordinal = 0), remap = false)
    private void allowSpyglassUse(InteractionHand hand, Player player, MutableObject<InteractionResult> object, int value, CallbackInfoReturnable<ServerboundUseItemPacket> cir, @Local ItemStack stack) {
        if (stack.getItem() instanceof SpyglassItem && Minecraft.getInstance().level != null) {
            InteractionResultHolder<ItemStack> result = stack.use(Minecraft.getInstance().level, player, hand);
            ItemStack stack1 = result.getObject();
            if (stack1 != stack) {
                player.setItemInHand(hand, stack1);
            }
            object.setValue(result.getResult());
        }
    }
}
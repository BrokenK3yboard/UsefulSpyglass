package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.config.CommonConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.overlay.WailaTickHandler;

@Pseudo
@Mixin(WailaTickHandler.class)
public class WailaTickHandlerMixin {

    @WrapOperation(method = "tickClient", at = @At(value = "INVOKE", target = "Lsnownee/jade/api/config/IWailaConfig$IConfigGeneral;shouldDisplayTooltip()Z"), require = 0)
    private boolean shouldDisplayToolttip(IWailaConfig.IConfigGeneral instance, Operation<Boolean> original) {
        if (CommonConfig.JADE_REQUIRES_SPYGLASS.get() && Minecraft.getInstance().player != null) {
            return original.call(instance) && Minecraft.getInstance().player.isScoping();
        } else {
            return original.call(instance);
        }
    }
}

package com.brokenkeyboard.usefulspyglass.mixin;

import com.brokenkeyboard.usefulspyglass.MarkedEntities;
import com.brokenkeyboard.usefulspyglass.MarkedEntitiesAccess;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements MarkedEntitiesAccess {

    @Unique
    private MarkedEntities US$MARKED_ENTITIES;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void initMarkedEntities(MinecraftServer server, ServerLevel level, GameProfile profile, ClientInformation info, CallbackInfo ci) {
        US$MARKED_ENTITIES = new MarkedEntities((ServerPlayer) (Object) this);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/warden/WardenSpawnTracker;tick()V"))
    public void tickMarkedEntities(CallbackInfo ci) {
        US$MARKED_ENTITIES.tick();
    }

    @Override
    public MarkedEntities us$getMarkedEntities() {
        return US$MARKED_ENTITIES;
    }
}
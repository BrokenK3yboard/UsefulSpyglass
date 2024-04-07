package com.brokenkeyboard.usefulspyglass;

import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

import static com.brokenkeyboard.usefulspyglass.Constants.PRECISION_SPYGLASS;

public class Curios {

    public static boolean checkCurios(Player player) {
        Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player).resolve();
        return handler.isPresent() && handler.get().findFirstCurio(PRECISION_SPYGLASS).isPresent();
    }
}
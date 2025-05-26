package com.brokenkeyboard.usefulspyglass;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.function.Predicate;

public class Curios {

    public static boolean checkCurios(Player player, Predicate<ItemStack> predicate) {
        Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player);
        return handler.isPresent() && handler.get().findFirstCurio(predicate).isPresent();
    }
}
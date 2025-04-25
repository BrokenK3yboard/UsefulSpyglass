package com.brokenkeyboard.usefulspyglass;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;
import java.util.function.Predicate;

public class Curios {

    public static boolean checkCurios(Player player, Predicate<ItemStack> predicate) {
        Optional<IItemHandlerModifiable> handler = CuriosApi.getCuriosHelper().getEquippedCurios(player).resolve();
        if (handler.isPresent()) {
            for (int i = 0; i < handler.get().getSlots(); i++) {
                if (predicate.test(handler.get().getStackInSlot(i))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
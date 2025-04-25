package com.brokenkeyboard.usefulspyglass;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

public class Trinkets {

    public static boolean checkTrinkets(Player player, Predicate<ItemStack> predicate) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        return component.isPresent() && component.get().isEquipped(predicate);
    }
}
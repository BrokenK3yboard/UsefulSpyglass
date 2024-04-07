package com.brokenkeyboard.usefulspyglass;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

import static com.brokenkeyboard.usefulspyglass.Constants.PRECISION_SPYGLASS;

public class Trinkets {

    public static boolean checkTrinkets(Player player) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        return component.isPresent() && component.get().isEquipped(PRECISION_SPYGLASS);
    }
}
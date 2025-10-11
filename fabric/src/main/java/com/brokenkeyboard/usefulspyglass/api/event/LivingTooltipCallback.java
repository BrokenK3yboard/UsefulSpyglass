package com.brokenkeyboard.usefulspyglass.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface LivingTooltipCallback {

    Event<LivingTooltipCallback> EVENT = EventFactory.createArrayBacked(LivingTooltipCallback.class,
            ((listeners) -> (LivingEntity entity, List<ClientTooltipComponent> tooltipInfoList) -> {
                for (LivingTooltipCallback listener : listeners) {
                    listener.livingTooltipCallback(entity, tooltipInfoList);
                }
            }));

    void livingTooltipCallback(LivingEntity entity, List<ClientTooltipComponent> tooltipInfoList);
}

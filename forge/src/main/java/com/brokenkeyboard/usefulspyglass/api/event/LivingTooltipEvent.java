package com.brokenkeyboard.usefulspyglass.api.event;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class LivingTooltipEvent extends Event {

    private final LivingEntity ENTITY;
    private final List<ClientTooltipComponent> TOOLTIP_INFO_LIST;

    public LivingTooltipEvent(LivingEntity entity, List<ClientTooltipComponent> tooltipInfoList) {
        ENTITY = entity;
        TOOLTIP_INFO_LIST = tooltipInfoList;
    }

    public LivingEntity getEntity() {
        return ENTITY;
    }

    public List<ClientTooltipComponent> getTooltipList() {
        return TOOLTIP_INFO_LIST;
    }
}

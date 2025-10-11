package com.brokenkeyboard.usefulspyglass.api.event;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;

import java.util.List;

public class BlockTooltipEvent extends Event {

    private final BlockState STATE;
    private final List<ClientTooltipComponent> TOOLTIP_INFO_LIST;

    public BlockTooltipEvent(BlockState state, List<ClientTooltipComponent> tooltipInfoList) {
        this.STATE = state;
        this.TOOLTIP_INFO_LIST = tooltipInfoList;
    }

    public BlockState getBlockState() {
        return STATE;
    }

    public List<ClientTooltipComponent> getTooltipList() {
        return TOOLTIP_INFO_LIST;
    }
}

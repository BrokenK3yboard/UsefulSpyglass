package com.brokenkeyboard.usefulspyglass.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface BlockTooltipCallback {

    Event<BlockTooltipCallback> EVENT = EventFactory.createArrayBacked(BlockTooltipCallback.class,
            ((listeners) -> (BlockState state, BlockPos pos, List<ClientTooltipComponent> tooltipInfoList) -> {
                for (BlockTooltipCallback listener : listeners) {
                    listener.blockTooltipEvent(state, pos, tooltipInfoList);
                }
            }));

    void blockTooltipEvent(BlockState state, BlockPos pos, List<ClientTooltipComponent> tooltipInfoList);
}

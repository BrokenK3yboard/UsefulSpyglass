package com.brokenkeyboard.usefulspyglass;

import net.minecraft.world.item.ItemStack;

public class SpyglassItem extends net.minecraft.world.item.SpyglassItem {

    public SpyglassItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public int getEnchantmentValue() {
        return 1;
    }
}

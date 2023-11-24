package com.brokenkeyboard.usefulspyglass.platform;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public ConcurrentMap<String, String> getModList() {
        ConcurrentMap<String, String> map = Maps.newConcurrentMap();
        List<IModInfo> list = ModList.get().getMods();
        for (IModInfo mod : list) {
            String modid = mod.getModId();
            String name = mod.getDisplayName() != null ? mod.getDisplayName() : modid;
            map.put(modid, name);
        }
        return map;
    }
}
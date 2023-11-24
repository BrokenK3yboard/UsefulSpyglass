package com.brokenkeyboard.usefulspyglass.platform;

import com.google.common.collect.Maps;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public ConcurrentMap<String, String> getModList() {
        ConcurrentMap<String, String> map = Maps.newConcurrentMap();
        List<ModContainer> mods = (List<ModContainer>) FabricLoader.getInstance().getAllMods();
        for (ModContainer mod : mods) {
            String modid = mod.getMetadata().getId();
            String name = mod.getMetadata().getName() != null ? mod.getMetadata().getName() : modid;
            map.put(modid, name);
        }
        return map;
    }
}
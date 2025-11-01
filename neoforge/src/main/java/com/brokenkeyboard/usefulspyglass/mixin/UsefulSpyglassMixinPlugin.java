package com.brokenkeyboard.usefulspyglass.mixin;

import com.google.common.collect.ImmutableMap;
import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class UsefulSpyglassMixinPlugin implements IMixinConfigPlugin {

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "com.brokenkeyboard.usefulspyglass.mixin.RayTracingMixin", () -> LoadingModList.get().getModFileById("jade") != null,
            "com.brokenkeyboard.usefulspyglass.mixin.WailaTickHandlerMixin", () -> LoadingModList.get().getModFileById("jade") != null
    );

    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClass, String mixinClass) {
        return CONDITIONS.getOrDefault(mixinClass, () -> true).get();
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class VPMixinPlugin implements IMixinConfigPlugin {
    private static final Map<String, String> MIXINS_TO_MODS = ImmutableMap.<String, String>builder()
            .put("juuxel.vanillaparts.mixin.extrapieces.SidingPieceBlockMixin", "extrapieces")
            .build();

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!MIXINS_TO_MODS.containsKey(mixinClassName)) return true;
        return FabricLoader.getInstance().isModLoaded(MIXINS_TO_MODS.get(mixinClassName));
    }


    /* +--------------------+
       | Boilerplate below! |
       +--------------------+ */

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}

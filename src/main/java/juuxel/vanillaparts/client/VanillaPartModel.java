/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.client;

import alexiil.mc.lib.multipart.api.render.PartModelBaker;
import alexiil.mc.lib.multipart.api.render.PartRenderContext;
import juuxel.vanillaparts.part.model.VanillaModelKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Environment(EnvType.CLIENT)
public enum VanillaPartModel implements PartModelBaker<VanillaModelKey> {
    INSTANCE;

    /**
     * A cache for ForwardingBakedModels that wrap the original models.
     * Multipart-format blockstate files create models that don't really work with passing
     * the model to the fallback consumer, so we have to create wrapping models that give the part's corresponding
     * block state to BakedModel.getQuads.
     */
    private final Map<BlockState, BakedModel> modelWrappers = new HashMap<>();

    private BakedModel getWrapper(VanillaModelKey key) {
        return modelWrappers.computeIfAbsent(
                key.getState(),
                state -> new ForwardingBakedModel() {
                    {
                        wrapped = MinecraftClient.getInstance()
                                .getBakedModelManager()
                                .getBlockModels()
                                .getModel(key.getState());
                    }

                    @Override
                    public List<BakedQuad> getQuads(BlockState blockState, Direction face, Random rand) {
                        return super.getQuads(state, face, rand);
                    }
                }
        );
    }

    @Override
    public void emitQuads(VanillaModelKey key, PartRenderContext ctx) {
        ctx.fallbackConsumer().accept(getWrapper(key));
    }

    /**
     * Used to clear the internal model cache when resources are reloaded.
     */
    void clearCache() {
        modelWrappers.clear();
    }
}

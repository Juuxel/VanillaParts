/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.client;

import alexiil.mc.lib.multipart.api.render.PartModelBaker;
import alexiil.mc.lib.multipart.api.render.PartRenderContext;
import juuxel.vanillaparts.part.model.VanillaModelKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public enum VanillaPartModel implements PartModelBaker<VanillaModelKey> {
    INSTANCE;

    @Override
    public void emitQuads(VanillaModelKey key, PartRenderContext ctx) {
        ctx.fallbackConsumer().accept(
                MinecraftClient.getInstance()
                        .getBakedModelManager()
                        .getBlockStateMaps()
                        .getModel(key.getState())
        );
    }
}

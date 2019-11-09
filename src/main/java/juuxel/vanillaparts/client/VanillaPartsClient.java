/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.client;

import alexiil.mc.lib.multipart.api.render.MultipartRenderRegistry;
import juuxel.vanillaparts.part.model.DynamicVanillaModelKey;
import juuxel.vanillaparts.part.model.StaticVanillaModelKey;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class VanillaPartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MultipartRenderRegistry.registerBaker(DynamicVanillaModelKey.class, VanillaPartModel.INSTANCE);
        MultipartRenderRegistry.registerBaker(StaticVanillaModelKey.class, VanillaPartModel.INSTANCE);
    }
}

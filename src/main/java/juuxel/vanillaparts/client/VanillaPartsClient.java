/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.client;

import alexiil.mc.lib.multipart.api.render.PartStaticModelRegisterEvent;
import juuxel.vanillaparts.part.model.VanillaModelKey;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public final class VanillaPartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PartStaticModelRegisterEvent.EVENT.register(model -> {
            model.register(VanillaModelKey.class, VanillaPartModel.INSTANCE);
        });

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
            .registerReloadListener(ModelReloadListener.INSTANCE);
    }
}

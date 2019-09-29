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

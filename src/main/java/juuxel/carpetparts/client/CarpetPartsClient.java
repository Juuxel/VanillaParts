package juuxel.carpetparts.client;

import alexiil.mc.lib.multipart.api.render.MultipartRenderRegistry;
import juuxel.carpetparts.CarpetPart;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class CarpetPartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MultipartRenderRegistry.registerBaker(CarpetPart.ModelKey.class, CarpetPartModel.INSTANCE);
    }
}

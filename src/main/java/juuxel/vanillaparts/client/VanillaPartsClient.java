package juuxel.vanillaparts.client;

import alexiil.mc.lib.multipart.api.render.MultipartRenderRegistry;
import juuxel.vanillaparts.part.CarpetPart;
import juuxel.vanillaparts.part.SlabPart;
import juuxel.vanillaparts.part.TorchPart;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class VanillaPartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MultipartRenderRegistry.registerBaker(CarpetPart.ModelKey.class, CarpetPartModel.INSTANCE);
        MultipartRenderRegistry.registerBaker(TorchPart.ModelKey.class, TorchPartModel.INSTANCE);
        MultipartRenderRegistry.registerBaker(SlabPart.ModelKey.class, SlabPartModel.INSTANCE);
    }
}

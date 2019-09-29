package juuxel.vanillaparts.client;

import alexiil.mc.lib.multipart.api.render.PartModelBaker;
import alexiil.mc.lib.multipart.api.render.PartRenderContext;
import juuxel.vanillaparts.part.CarpetPart;
import juuxel.vanillaparts.VanillaParts;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public enum CarpetPartModel implements PartModelBaker<CarpetPart.ModelKey> {
    INSTANCE;

    @Override
    public void emitQuads(CarpetPart.ModelKey key, PartRenderContext ctx) {
        ctx.fallbackConsumer().accept(
                MinecraftClient.getInstance()
                        .getBakedModelManager()
                        .getBlockStateMaps()
                        .getModel(VanillaParts.CARPETS.get(key.getColor()).getDefaultState())
        );
    }
}

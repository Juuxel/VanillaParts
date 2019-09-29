package juuxel.vanillaparts;

import juuxel.vanillaparts.api.VanillaPartsInitializer;
import juuxel.vanillaparts.block.VBlocks;
import juuxel.vanillaparts.part.VPartDefinitions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.util.List;

public final class VanillaParts implements ModInitializer {

    public static Identifier id(String path) {
        return new Identifier("vanilla_parts", path);
    }

    @Override
    public void onInitialize() {
        VBlocks.init();
        VPartDefinitions.init();

        // Load VP initializers
        List<VanillaPartsInitializer> initializers = FabricLoader.getInstance()
                .getEntrypoints("vanilla_parts", VanillaPartsInitializer.class);

        for (VanillaPartsInitializer initializer : initializers) {
            initializer.onCarpetPartInitialize();
        }

        // Register carpet and torch item tweak
        UseBlockCallback.EVENT.register(MultipartItemTweak.INSTANCE);
    }
}

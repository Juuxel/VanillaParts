package juuxel.vanillaparts;

import juuxel.vanillaparts.part.VPartDefinitions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.Identifier;

public final class VanillaParts implements ModInitializer {
    public static Identifier id(String path) {
        return new Identifier("vanilla_parts", path);
    }

    @Override
    public void onInitialize() {
        VPartDefinitions.init();

        // Register carpet and torch item tweak
        UseBlockCallback.EVENT.register(MultipartItemTweak.INSTANCE);
    }
}

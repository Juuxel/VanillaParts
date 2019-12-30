package juuxel.vanillaparts.client;

import com.google.common.collect.ImmutableList;
import juuxel.vanillaparts.VanillaParts;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.List;

/**
 * A {@code ResourceReloadListener} used for clearing the model cache of
 * {@link VanillaPartModel}.
 *
 * <p>The model cache has to be cleared every time client resources are
 * reloaded. If it's not cleared, the part models can be outdated or
 * even have broken textures (see <a href="https://github.com/Juuxel/VanillaParts/issues/6">issue #6</a>).
 */
@Environment(EnvType.CLIENT)
enum ModelReloadListener implements SimpleSynchronousResourceReloadListener {
    INSTANCE;

    private static final Identifier ID = VanillaParts.id("model_reload");
    private static final List<Identifier> DEPENDENCIES =
        ImmutableList.of(
            ResourceReloadListenerKeys.MODELS,
            ResourceReloadListenerKeys.TEXTURES
        );

    @Override
    public void apply(ResourceManager resourceManager) {
        VanillaPartModel.INSTANCE.clearCache();
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return DEPENDENCIES;
    }
}

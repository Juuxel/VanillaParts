package juuxel.vanillaparts.lib;

import juuxel.vanillaparts.VanillaParts;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public final class VpTags {
    public static final Tag<Block> EXCLUDED = TagRegistry.block(VanillaParts.id("excluded"));

    public static void init() {
    }
}

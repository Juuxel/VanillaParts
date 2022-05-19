package juuxel.vanillaparts.lib;

import juuxel.vanillaparts.VanillaParts;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public final class VpTags {
    public static final TagKey<Block> EXCLUDED = TagKey.of(Registry.BLOCK_KEY, VanillaParts.id("excluded"));

    public static void init() {
    }
}

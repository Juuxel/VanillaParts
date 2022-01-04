package juuxel.vanillaparts.lib;

import juuxel.vanillaparts.VanillaParts;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public final class VpTags {
    public static final Tag<Block> EXCLUDED = TagFactory.BLOCK.create(VanillaParts.id("excluded"));

    public static void init() {
    }
}

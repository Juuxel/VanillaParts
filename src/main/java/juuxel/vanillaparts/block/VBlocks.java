package juuxel.vanillaparts.block;

import juuxel.vanillaparts.VanillaParts;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public final class VBlocks {
    public static final Block CRAFTING_SLAB = new CraftingSlabBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).breakByTool(FabricToolTags.AXES).build());

    private VBlocks() {}

    public static void init() {
        Registry.register(Registry.BLOCK, VanillaParts.id("crafting_slab"), CRAFTING_SLAB);
        Registry.register(Registry.ITEM, VanillaParts.id("crafting_slab"), new BlockItem(CRAFTING_SLAB, new Item.Settings().group(ItemGroup.DECORATIONS)));
    }
}

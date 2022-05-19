package juuxel.vanillaparts.lib;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;

public final class Exclusions {
    public static boolean isExcluded(BlockState state) {
        return state.isIn(VpTags.EXCLUDED) || state.getBlock() instanceof BlockEntityProvider;
    }

    public static boolean isExcluded(Block block) {
        return block.getRegistryEntry().isIn(VpTags.EXCLUDED) || block instanceof BlockEntityProvider;
    }
}

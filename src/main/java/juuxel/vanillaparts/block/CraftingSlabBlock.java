package juuxel.vanillaparts.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CraftingSlabBlock extends SlabBlock {
    public CraftingSlabBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        player.openContainer(state.createContainerProvider(world, pos));
        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return true;
    }

    @Override
    public NameableContainerProvider createContainerProvider(BlockState state, World world, BlockPos pos) {
        return Blocks.CRAFTING_TABLE.getDefaultState().createContainerProvider(world, pos);
    }
}

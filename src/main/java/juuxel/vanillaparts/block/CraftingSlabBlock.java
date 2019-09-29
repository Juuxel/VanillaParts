package juuxel.vanillaparts.block;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.vanillaparts.part.CraftingSlabPart;
import juuxel.vanillaparts.part.VPartDefinitions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

// TODO: These don't double even with the tweak
public class CraftingSlabBlock extends SlabBlock implements NativeMultipart {
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

    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        return Collections.singletonList(holder -> new CraftingSlabPart(VPartDefinitions.CRAFTING_SLAB, holder, this, state.get(TYPE) == SlabType.TOP));
    }
}

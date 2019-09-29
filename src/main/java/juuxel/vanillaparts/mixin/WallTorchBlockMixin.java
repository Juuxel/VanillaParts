package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import juuxel.vanillaparts.VanillaParts;
import juuxel.vanillaparts.part.TorchPart;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(WallTorchBlock.class)
public class WallTorchBlockMixin extends TorchBlockMixin {
    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        return (Object) this == Blocks.WALL_TORCH ? Collections.singletonList(holder -> new TorchPart(VanillaParts.TORCH, holder, TorchPart.Facing.of(state.get(HorizontalFacingBlock.FACING)))) : null;
    }
}

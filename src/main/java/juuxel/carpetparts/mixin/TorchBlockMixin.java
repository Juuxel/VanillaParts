package juuxel.carpetparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.carpetparts.CarpetParts;
import juuxel.carpetparts.part.TorchPart;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(TorchBlock.class)
public class TorchBlockMixin implements NativeMultipart {
    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        return (Object) this == Blocks.TORCH ? Collections.singletonList(holder -> new TorchPart(CarpetParts.TORCH, holder, TorchPart.Facing.GROUND)) : null;
    }
}

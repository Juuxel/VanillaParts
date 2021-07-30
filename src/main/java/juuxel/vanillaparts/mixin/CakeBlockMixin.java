package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.vanillaparts.part.CakePart;
import juuxel.vanillaparts.part.VpParts;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(CakeBlock.class)
abstract class CakeBlockMixin implements NativeMultipart {
    @Nullable
    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos blockPos, BlockState blockState) {
        if ((Object) this == Blocks.CAKE) {
            return Collections.singletonList(holder -> new CakePart(VpParts.CAKE, holder, blockState.get(CakeBlock.BITES)));
        }

        return null;
    }
}

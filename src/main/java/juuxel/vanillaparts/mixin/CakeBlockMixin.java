package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.vanillaparts.part.CakePart;
import juuxel.vanillaparts.part.VPartDefinitions;
import net.minecraft.block.BlockState;
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
        return Collections.singletonList(holder -> new CakePart(VPartDefinitions.CAKE, holder, blockState.get(CakeBlock.BITES)));
    }
}

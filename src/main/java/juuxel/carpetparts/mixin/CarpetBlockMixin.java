package juuxel.carpetparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.carpetparts.CarpetPart;
import juuxel.carpetparts.CarpetParts;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.List;

@Mixin(CarpetBlock.class)
public abstract class CarpetBlockMixin implements NativeMultipart {
    @Shadow
    public abstract DyeColor getColor();

    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        DyeColor color = getColor();
        return Collections.singletonList(holder -> new CarpetPart(CarpetParts.CARPET_PARTS.get(color), holder, color));
    }
}

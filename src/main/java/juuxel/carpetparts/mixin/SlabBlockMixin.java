package juuxel.carpetparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.carpetparts.CarpetParts;
import juuxel.carpetparts.part.SlabPart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(SlabBlock.class)
public class SlabBlockMixin extends Block implements NativeMultipart {
    private SlabBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        SlabType type = state.get(SlabBlock.TYPE);
        if (type == SlabType.DOUBLE) return null;
        return Collections.singletonList(holder -> new SlabPart(CarpetParts.SLAB_PARTS.get(this), holder, (SlabBlock) (Object) this, type == SlabType.TOP));
    }
}

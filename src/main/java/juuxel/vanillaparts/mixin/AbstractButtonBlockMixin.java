package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.vanillaparts.part.ButtonPart;
import juuxel.vanillaparts.part.VPartDefinitions;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(AbstractButtonBlock.class)
public class AbstractButtonBlockMixin extends WallMountedBlock implements NativeMultipart {
    private AbstractButtonBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        return Collections.singletonList(holder -> new ButtonPart(VPartDefinitions.BUTTON_PARTS.get(this), holder, this, state.get(FACE), state.get(FACING)));
    }
}

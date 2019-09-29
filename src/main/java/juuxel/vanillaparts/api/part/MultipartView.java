package juuxel.vanillaparts.api.part;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface MultipartView {
    World getWorld();
    BlockPos getPos();

    MultipartState getMultipartState();

    @Nullable
    MultipartEntity getMultipartEntity();

    interface Mutable extends MultipartView {
        void setMultipartState(MultipartState state);
    }
}

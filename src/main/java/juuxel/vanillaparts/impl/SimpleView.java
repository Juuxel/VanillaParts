package juuxel.vanillaparts.impl;

import juuxel.vanillaparts.api.part.MultipartEntity;
import juuxel.vanillaparts.api.part.MultipartState;
import juuxel.vanillaparts.api.part.MultipartView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

final class SimpleView implements MultipartView.Mutable {
    World world;
    BlockPos pos;
    MultipartState state;
    @Nullable MultipartEntity entity;

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public MultipartState getMultipartState() {
        return state;
    }

    @Nullable
    @Override
    public MultipartEntity getMultipartEntity() {
        return entity;
    }

    @Override
    public void setMultipartState(MultipartState state) {
        this.state = state;
    }
}

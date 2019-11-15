/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.property.MultipartProperties;
import alexiil.mc.lib.multipart.api.property.MultipartPropertyContainer;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import juuxel.blockstoparts.model.DynamicVanillaModelKey;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.EmptyBlockView;

public abstract class WallMountedRedstonePart extends VanillaPart {
    protected final WallMountLocation face;
    protected final Direction facing;
    protected boolean powered;

    protected WallMountedRedstonePart(PartDefinition definition, MultipartHolder holder, WallMountLocation face, Direction facing, boolean powered) {
        super(definition, holder);
        this.face = face;
        this.facing = facing;
        this.powered = powered;
    }

    @Override
    public VoxelShape getShape() {
        return getBlockState().getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
    }

    @Override
    public VoxelShape getCollisionShape() {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getDynamicShape(float partialTicks) {
        return getShape();
    }

    @Override
    public PartModelKey getModelKey() {
        return new DynamicVanillaModelKey(this);
    }

    protected Direction getActualFacing() {
        switch (face) {
            case CEILING:
                return Direction.UP;
            case FLOOR:
                return Direction.DOWN;
            case WALL:
            default:
                return facing.getOpposite();
        }
    }

    protected void updateRedstoneLevels() {
        int power = powered ? 15 : 0;
        MultipartPropertyContainer props = this.holder.getContainer().getProperties();
        Direction actualFacing = getActualFacing();
        props.setValue(this, MultipartProperties.getStrongRedstonePower(actualFacing), power);
        for (Direction direction : Direction.values()) {
            if (direction == actualFacing) continue;
            props.setValue(this, MultipartProperties.getWeakRedstonePower(direction), power);
        }
    }

    protected final void updateListeners() {
        BlockPos pos = getPos();
        BlockState multipartState = getWorld().getBlockState(pos);
        getWorld().updateListeners(getPos(), multipartState, multipartState, 3);
    }
}

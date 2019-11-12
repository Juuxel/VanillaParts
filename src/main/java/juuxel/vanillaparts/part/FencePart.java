/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import juuxel.vanillaparts.util.FenceExtensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class FencePart extends HorizontallyConnectedPart {
    private static final VoxelShape POST_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 16, 10);

    // Automatically calculates connections
    public FencePart(PartDefinition definition, MultipartHolder holder, Block fence) {
        super(definition, holder, fence);
    }

    public FencePart(PartDefinition definition, MultipartHolder holder, Block fence, boolean north, boolean east, boolean south, boolean west) {
        super(definition, holder, fence, north, east, south, west);
    }

    @Override
    public VoxelShape getShape() {
        return POST_SHAPE;
    }

    @Override
    protected boolean canConnectTo(BlockPos neighborPos, Direction d) {
        BlockState state = getWorld().getBlockState(neighborPos);
        Direction sideOfOther = d.getOpposite();
        boolean isSideSolidFullSquare = state.isSideSolidFullSquare(getWorld(), neighborPos, sideOfOther);
        return ((FenceBlock) block).canConnect(state, isSideSolidFullSquare, sideOfOther) || ((FenceExtensions) block).vanillaParts_canConnect(getWorld(), neighborPos, sideOfOther);
    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.compat.extrapieces;

import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import alexiil.mc.lib.net.IMsgWriteCtx;
import alexiil.mc.lib.net.NetByteBuf;
import com.shnupbups.extrapieces.blocks.SidingPieceBlock;
import juuxel.vanillaparts.part.VanillaPart;
import juuxel.vanillaparts.part.model.StaticVanillaModelKey;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EmptyBlockView;

public class SidingPart extends VanillaPart {
    private Block siding;
    private Direction facing;

    public SidingPart(PartDefinition definition, MultipartHolder holder, Block siding, Direction facing) {
        super(definition, holder);
        this.siding = siding;
        this.facing = facing;
    }

    public SidingPart(PartDefinition definition, MultipartHolder holder, Block siding, int facing) {
        this(definition, holder, siding, Util.safeGet(Direction.values(), facing));
    }

    @Override
    public BlockState getVanillaState() {
        return siding.getDefaultState().with(SidingPieceBlock.FACING_HORIZONTAL, facing);
    }

    @Override
    public VoxelShape getShape() {
        return getVanillaState().getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
    }

    @Override
    public PartModelKey getModelKey() {
        return new StaticVanillaModelKey(getVanillaState());
    }

    @Override
    public CompoundTag toTag() {
        return Util.with(super.toTag(), tag -> {
            tag.putInt("Facing", facing.getId());
        });
    }

    @Override
    public void writeCreationData(NetByteBuf buffer, IMsgWriteCtx ctx) {
        super.writeCreationData(buffer, ctx);
        buffer.writeEnumConstant(facing);
    }
}

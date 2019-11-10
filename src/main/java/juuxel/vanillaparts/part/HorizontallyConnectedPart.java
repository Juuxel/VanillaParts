/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import alexiil.mc.lib.net.*;
import juuxel.vanillaparts.part.model.DynamicVanillaModelKey;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalConnectedBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EmptyBlockView;

public abstract class HorizontallyConnectedPart extends VanillaPart {
    public static final ParentNetIdSingle<HorizontallyConnectedPart> NET_HORIZONTALLY_CONNECTED;
    public static final NetIdDataK<HorizontallyConnectedPart> CONNECTION_DATA;

    static {
        NET_HORIZONTALLY_CONNECTED = NET_ID.subType(HorizontallyConnectedPart.class, "vanilla_parts:horizontally_connected");
        CONNECTION_DATA = NET_HORIZONTALLY_CONNECTED.idData("connection_data").setReceiver(HorizontallyConnectedPart::updateConnections);
    }

    private boolean north = false;
    private boolean east = false;
    private boolean south = false;
    private boolean west = false;
    protected final HorizontalConnectedBlock block;

    public HorizontallyConnectedPart(PartDefinition definition, MultipartHolder holder, Block block) {
        super(definition, holder);
        this.block = (HorizontalConnectedBlock) block;
    }

    public HorizontallyConnectedPart(PartDefinition definition, MultipartHolder holder, Block block, boolean north, boolean east, boolean south, boolean west) {
        this(definition, holder, block);
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public HorizontalConnectedBlock getBlock() {
        return block;
    }

    @Override
    public BlockState getVanillaState() {
        return block.getDefaultState()
                .with(HorizontalConnectedBlock.NORTH, north)
                .with(HorizontalConnectedBlock.EAST, east)
                .with(HorizontalConnectedBlock.SOUTH, south)
                .with(HorizontalConnectedBlock.WEST, west);
    }

    @Override
    public VoxelShape getShape() {
        return getVanillaState().getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
    }

    @Override
    public VoxelShape getDynamicShape(float partialTicks) {
        return getShape();
    }

    @Override
    public VoxelShape getCollisionShape() {
        return getVanillaState().getCollisionShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
    }

    @Override
    public PartModelKey getModelKey() {
        return new DynamicVanillaModelKey(this);
    }

    protected abstract boolean canConnectTo(BlockPos neighborPos, Direction d);

    private boolean getConnection(Direction d) {
        switch (d) {
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case WEST:
                return west;
            case EAST:
            default:
                return east;
        }
    }

    private void setConnection(Direction d, boolean connected) {
        switch (d) {
            case NORTH:
                north = connected;
                break;
            case SOUTH:
                south = connected;
                break;
            case WEST:
                west = connected;
                break;
            case EAST:
                east = connected;
                break;
        }
    }

    private void updateConnections(NetByteBuf buf, IMsgReadCtx ctx) {
        north = buf.readBoolean();
        east = buf.readBoolean();
        south = buf.readBoolean();
        west = buf.readBoolean();
        holder.getContainer().redrawIfChanged();
    }

    @Override
    protected void onNeighborUpdate(BlockPos neighborPos) {
        Direction side = Util.compare(getPos(), neighborPos);
        boolean canConnect = canConnectTo(neighborPos, side);
        System.out.println("Side: " + side + ", can connect? " + canConnect + ", connection: " + getConnection(side));
        if (getConnection(side) != canConnect) {
            setConnection(side, canConnect);
            System.out.println("again: Side: " + side + ", can connect? " + canConnect + ", connection: " + getConnection(side));
            holder.getContainer().sendNetworkUpdate(this, CONNECTION_DATA, (obj, buf, ctx) -> {
                buf.writeBoolean(north).writeBoolean(east).writeBoolean(south).writeBoolean(west);
            });
            holder.getContainer().redrawIfChanged();
        }
    }

    @Override
    public CompoundTag toTag() {
        return Util.with(super.toTag(), tag -> {
            tag.putBoolean("North", north);
            tag.putBoolean("East", east);
            tag.putBoolean("South", south);
            tag.putBoolean("West", west);
        });
    }

    @Override
    public void writeCreationData(NetByteBuf buf, IMsgWriteCtx ctx) {
        super.writeCreationData(buf, ctx);
        buf.writeBoolean(north).writeBoolean(east).writeBoolean(south).writeBoolean(west);
    }
}

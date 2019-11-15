/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartEventBus;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.event.PartAddedEvent;
import alexiil.mc.lib.multipart.api.event.PartTickEvent;
import alexiil.mc.lib.multipart.api.property.MultipartProperties;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import alexiil.mc.lib.net.IMsgWriteCtx;
import alexiil.mc.lib.net.NetByteBuf;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import juuxel.blockstoparts.model.StaticVanillaModelKey;
import juuxel.blockstoparts.part.Categories;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.Locale;

public class TorchPart extends VanillaPart {
    private static final ImmutableMap<Facing, VoxelShape> SHAPES;
    private final Facing facing;

    static {
        SHAPES = ImmutableMap.of(
                Facing.GROUND, Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 10.0, 10.0),
                Facing.NORTH, Block.createCuboidShape(5.5, 3.0, 11.0, 10.5, 13.0, 16.0),
                Facing.SOUTH, Block.createCuboidShape(5.5, 3.0, 0.0, 10.5, 13.0, 5.0),
                Facing.WEST, Block.createCuboidShape(11.0, 3.0, 5.5, 16.0, 13.0, 10.5),
                Facing.EAST, Block.createCuboidShape(0.0, 3.0, 5.5, 5.0, 13.0, 10.5)
        );
    }

    public TorchPart(PartDefinition definition, MultipartHolder holder, Facing facing) {
        super(definition, holder);
        this.facing = facing;
    }

    public TorchPart(PartDefinition definition, MultipartHolder holder, CompoundTag tag) {
        super(definition, holder);
        Facing facing;
        try {
            facing = Facing.valueOf(tag.getString("Facing").toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            facing = Facing.GROUND;
        }
        this.facing = facing;
    }

    public TorchPart(PartDefinition definition, MultipartHolder holder, byte facing) {
        super(definition, holder);
        this.facing = Facing.values()[MathHelper.clamp(facing, 0, 4)];
    }

    @Override
    public VoxelShape getShape() {
        return SHAPES.get(facing);
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
    public ItemStack getPickStack() {
        return new ItemStack(Blocks.TORCH);
    }

    @Override
    public PartModelKey getModelKey() {
        return new StaticVanillaModelKey(getBlockState());
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        super.onAdded(bus);
        bus.addListener(
                this, PartAddedEvent.class,
                event -> this.holder.getContainer().getProperties().setValue(this, MultipartProperties.LIGHT_VALUE, 15)
        );
        bus.addContextlessListener(
                this, PartTickEvent.class,
                () -> {
                    World world = getWorld();
                    if (world.isClient && world.random.nextInt(10) == 0) {
                        BlockState state = getBlockState();
                        state.getBlock().randomDisplayTick(state, world, getPos(), world.random);
                    }
                }
        );
    }

    @Override
    public BlockState getBlockState() {
        return facing == Facing.GROUND ? Blocks.TORCH.getDefaultState() : Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, facing.getDirection());
    }

    @Override
    public CompoundTag toTag() {
        return DataFixUtils.make(new CompoundTag(), tag -> {
            tag.putString("Facing", facing.toString().toLowerCase(Locale.ROOT));
        });
    }

    @Override
    public void writeCreationData(NetByteBuf buffer, IMsgWriteCtx ctx) {
        super.writeCreationData(buffer, ctx);
        buffer.writeByte((byte) facing.ordinal());
    }

    @Override
    public Categories getCategories() {
        return VPCategories.TORCH;
    }

    public enum Facing {
        GROUND(Direction.DOWN),
        NORTH(Direction.NORTH),
        EAST(Direction.EAST),
        SOUTH(Direction.SOUTH),
        WEST(Direction.WEST);

        private final Direction direction;

        Facing(Direction direction) {
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        public static Facing of(Direction direction) {
            switch (direction) {
                case NORTH:
                    return Facing.NORTH;
                case SOUTH:
                    return Facing.SOUTH;
                case WEST:
                    return Facing.WEST;
                case EAST:
                    return Facing.EAST;
                case DOWN:
                case UP:
                default:
                    return Facing.GROUND;
            }
        }
    }
}

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
import juuxel.blockstoparts.api.category.CategorySet;
import juuxel.blockstoparts.api.model.StaticVanillaModelKey;
import juuxel.vanillaparts.util.NbtKeys;
import juuxel.vanillaparts.util.NbtUtil;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TorchPart extends VanillaPart {
    private static final ImmutableMap<Facing, VoxelShape> SHAPES;
    private final Block block;
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

    public TorchPart(PartDefinition definition, MultipartHolder holder, Block groundBlock, Block wallBlock, Facing facing) {
        super(definition, holder);
        this.block = facing == Facing.GROUND ? groundBlock : wallBlock;
        this.facing = facing;
    }

    public static PartDefinition.IPartNbtReader fromNbt(Block groundBlock, Block wallBlock) {
        return (definition, holder, nbt) -> new TorchPart(definition, holder, groundBlock, wallBlock, NbtUtil.getEnum(nbt, NbtKeys.FACING, Facing.class));
    }

    public static PartDefinition.IPartNetLoader fromBuf(Block groundBlock, Block wallBlock) {
        return (definition, holder, buf, ctx) -> new TorchPart(definition, holder, groundBlock, wallBlock, buf.readEnumConstant(Facing.class));
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
    public VoxelShape getOutlineShape() {
        return getShape();
    }

    @Override
    public ItemStack getPickStack(@Nullable BlockHitResult hitResult) {
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
            event -> this.holder.getContainer().getProperties().setValue(this, MultipartProperties.LIGHT_VALUE, getBlockState().getLuminance())
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
        return facing == Facing.GROUND ? block.getDefaultState() : block.getDefaultState().with(WallTorchBlock.FACING, facing.getDirection());
    }

    @Override
    public NbtCompound toTag() {
        return Util.with(new NbtCompound(), tag -> {
            NbtUtil.putEnum(tag, NbtKeys.FACING, facing);
        });
    }

    @Override
    public void writeCreationData(NetByteBuf buffer, IMsgWriteCtx ctx) {
        buffer.writeEnumConstant(facing);
    }

    @Override
    protected void addCategories(CategorySet.Builder builder) {
        builder.add(VpCategories.TORCHES);
    }

    public enum Facing implements StringIdentifiable {
        GROUND(Direction.DOWN, "ground"),
        NORTH(Direction.NORTH, "north"),
        EAST(Direction.EAST, "east"),
        SOUTH(Direction.SOUTH, "south"),
        WEST(Direction.WEST, "west");

        private final Direction direction;
        private final String id;

        Facing(Direction direction, String id) {
            this.direction = direction;
            this.id = id;
        }

        public Direction getDirection() {
            return direction;
        }

        @Override
        public String asString() {
            return id;
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

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartEventBus;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.property.MultipartProperties;
import alexiil.mc.lib.multipart.api.property.MultipartPropertyContainer;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import alexiil.mc.lib.net.IMsgWriteCtx;
import alexiil.mc.lib.net.NetByteBuf;
import com.mojang.datafixers.DataFixUtils;
import juuxel.vanillaparts.mixin.LeverBlockAccessor;
import juuxel.vanillaparts.part.model.DynamicVanillaModelKey;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EmptyBlockView;

// TODO: Levers power blocks through other parts
// TODO: emits_redstone
public class LeverPart extends VanillaPart {
    private final WallMountLocation face;
    private final Direction facing;
    private boolean powered;

    public LeverPart(PartDefinition definition, MultipartHolder holder, WallMountLocation face, Direction facing, boolean powered) {
        super(definition, holder);
        this.face = face;
        this.facing = facing;
        this.powered = powered;
    }

    public LeverPart(PartDefinition definition, MultipartHolder holder, CompoundTag tag) {
        this(definition, holder, readFace(tag.getInt("Face")), Direction.byId(tag.getInt("Facing")), tag.getBoolean("Powered"));
    }

    public LeverPart(PartDefinition definition, MultipartHolder holder, NetByteBuf buf) {
        this(definition, holder, readFace(buf.readByte()), Direction.byId(buf.readByte()), buf.readBoolean());
    }

    @Override
    public CompoundTag toTag() {
        return DataFixUtils.make(super.toTag(), tag -> {
            tag.putInt("Face", face.ordinal());
            tag.putInt("Facing", facing.getId());
            tag.putBoolean("Powered", powered);
        });
    }

    @Override
    public void writeCreationData(NetByteBuf buffer, IMsgWriteCtx ctx) {
        super.writeCreationData(buffer, ctx);
        buffer.writeByte((byte) face.ordinal());
        buffer.writeByte((byte) facing.getId());
        buffer.writeBoolean(powered);
    }

    @Override
    public BlockState getVanillaState() {
        return Blocks.LEVER.getDefaultState()
                .with(LeverBlock.FACE, face)
                .with(LeverBlock.FACING, facing)
                .with(LeverBlock.POWERED, powered);
    }

    @Override
    public VoxelShape getShape() {
        return getVanillaState().getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
    }

    @Override
    public PartModelKey getModelKey() {
        return new DynamicVanillaModelKey(this);
    }

    @Override
    public boolean onActivate(PlayerEntity player, Hand hand, BlockHitResult hit) {
        powered = !powered;
        updateRedstoneLevels(powered ? 15 : 0);
        if (player.world.isClient) {
            if (powered) {
                LeverBlockAccessor.callSpawnParticles(getVanillaState(), player.world, hit.getBlockPos(), 1f);
            }
        } else {
            float pitch = powered ? 0.6f : 0.5f;
            player.world.playSound(null, hit.getBlockPos(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, pitch);
        }
        BlockPos pos = getPos();
        BlockState multipartState = getWorld().getBlockState(pos);
        player.world.updateListeners(getPos(), multipartState, multipartState, 3);
        return true;
    }

    private Direction getActualFacing() {
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

    private void updateRedstoneLevels(int power) {
        MultipartPropertyContainer props = this.holder.getContainer().getProperties();
        Direction actualFacing = getActualFacing();
        props.setValue(this, MultipartProperties.getStrongRedstonePower(actualFacing), power);
        for (Direction direction : Direction.values()) {
            if (direction == actualFacing) continue;
            props.setValue(this, MultipartProperties.getWeakRedstonePower(direction), power);
        }
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        super.onAdded(bus);
        updateRedstoneLevels(0);
    }

    private static WallMountLocation readFace(int i) {
        return Util.safeGet(WallMountLocation.values(), i);
    }
}

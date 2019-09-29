package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartEventBus;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.event.PartTickEvent;
import alexiil.mc.lib.multipart.api.property.MultipartProperties;
import alexiil.mc.lib.multipart.api.property.MultipartPropertyContainer;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import alexiil.mc.lib.net.IMsgWriteCtx;
import alexiil.mc.lib.net.NetByteBuf;
import com.mojang.datafixers.DataFixUtils;
import juuxel.vanillaparts.mixin.AbstractButtonBlockAccessor;
import juuxel.vanillaparts.part.model.DynamicVanillaModelKey;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EmptyBlockView;

import java.util.List;

// TODO: Levers power blocks through other parts
public class ButtonPart extends WallMountedRedstonePart {
    private final Block block;
    private final AbstractButtonBlockAccessor buttonBlock;
    private int timer = 0;

    public ButtonPart(PartDefinition definition, MultipartHolder holder, Block buttonBlock, WallMountLocation face, Direction facing) {
        super(definition, holder, face, facing, false);
        this.block = buttonBlock;
        this.buttonBlock = (AbstractButtonBlockAccessor) buttonBlock;
    }

    public ButtonPart(PartDefinition definition, MultipartHolder holder, Block buttonBlock, CompoundTag tag) {
        this(definition, holder, buttonBlock, readFace(tag.getInt("Face")), Direction.byId(tag.getInt("Facing")));
        this.powered = tag.getBoolean("Powered");
        this.timer = tag.getInt("Timer");
    }

    public ButtonPart(PartDefinition definition, MultipartHolder holder, Block buttonBlock, NetByteBuf buf) {
        this(definition, holder, buttonBlock, readFace(buf.readByte()), Direction.byId(buf.readByte()));
    }

    @Override
    public CompoundTag toTag() {
        return DataFixUtils.make(super.toTag(), tag -> {
            tag.putInt("Face", face.ordinal());
            tag.putInt("Facing", facing.getId());
            tag.putBoolean("Powered", powered);
            tag.putInt("Timer", timer);
        });
    }

    @Override
    public void writeCreationData(NetByteBuf buffer, IMsgWriteCtx ctx) {
        super.writeCreationData(buffer, ctx);
        buffer.writeByte((byte) face.ordinal());
        buffer.writeByte((byte) facing.getId());
    }

    @Override
    public BlockState getVanillaState() {
        return block.getDefaultState()
                .with(AbstractButtonBlock.FACE, face)
                .with(AbstractButtonBlock.FACING, facing)
                .with(AbstractButtonBlock.POWERED, powered);
    }

    private void tick() {
        if (timer > 0) {
            timer--;
        }

        if (buttonBlock.isWooden() && (!powered || timer <= 0)) {
            List<? extends Entity> entities = getWorld().getEntities(ProjectileEntity.class, getShape().getBoundingBox().offset(getPos()));
            boolean hasEntities = !entities.isEmpty();
            if (hasEntities != powered) {
                powered = hasEntities;
                if (!getWorld().isClient) {
                    updateRedstoneLevels(powered ? 15 : 0);
                    buttonBlock.callPlayClickSound(null, getWorld(), getPos(), hasEntities);
                }
                updateListeners();
                if (powered) {
                    timer = block.getTickRate(getWorld());
                }
            }
        } else if (timer <= 0 && powered) {
            powered = false;
            if (!getWorld().isClient) {
                updateRedstoneLevels(0);
                buttonBlock.callPlayClickSound(null, getWorld(), getPos(), false);
            }
            updateListeners();
        }
    }

    @Override
    public boolean onActivate(PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!powered) {
            timer = block.getTickRate(player.world);
            powered = true;
            if (!player.world.isClient) {
                updateRedstoneLevels(15);
            }
            buttonBlock.callPlayClickSound(player, player.world, hit.getBlockPos(), true);
            updateListeners();
        }
        return true;
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        super.onAdded(bus);
        updateRedstoneLevels(0);
        MultipartPropertyContainer props = this.holder.getContainer().getProperties();
        props.setValue(this, MultipartProperties.CAN_EMIT_REDSTONE, true);
        bus.addContextlessListener(this, PartTickEvent.class, this::tick);
    }

    private static WallMountLocation readFace(int i) {
        return Util.safeGet(WallMountLocation.values(), i);
    }
}

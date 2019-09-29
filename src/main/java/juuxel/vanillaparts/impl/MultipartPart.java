package juuxel.vanillaparts.impl;

import alexiil.mc.lib.multipart.api.AbstractPart;
import alexiil.mc.lib.multipart.api.MultipartEventBus;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.event.NeighbourUpdateEvent;
import alexiil.mc.lib.multipart.api.event.PartTickEvent;
import alexiil.mc.lib.multipart.api.property.MultipartProperties;
import alexiil.mc.lib.multipart.api.property.MultipartPropertyContainer;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import alexiil.mc.lib.net.IMsgWriteCtx;
import alexiil.mc.lib.net.NetByteBuf;
import com.mojang.datafixers.DataFixUtils;
import juuxel.vanillaparts.api.part.Multipart;
import juuxel.vanillaparts.api.part.MultipartEntity;
import juuxel.vanillaparts.api.part.MultipartState;
import juuxel.vanillaparts.api.part.MultipartView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.OptionalInt;

public class MultipartPart extends AbstractPart implements MultipartView.Mutable {
    private final Multipart multipart;
    @Nullable private final MultipartEntity entity;
    private MultipartState state;

    public MultipartPart(MultipartDefinition definition, MultipartHolder holder) {
        super(definition, holder);
        this.multipart = definition.getMultipart();
        this.entity = multipart.createEntity(this);
        this.state = multipart.getDefaultState();
    }

    public MultipartPart(MultipartDefinition definition, MultipartHolder holder, MultipartState state, @Nullable MultipartEntity entity) {
        super(definition, holder);
        this.multipart = definition.getMultipart();
        this.entity = entity;
        this.state = state;
    }

    @Override
    public void writeCreationData(NetByteBuf buffer, IMsgWriteCtx ctx) {
        super.writeCreationData(buffer, ctx);
        buffer.writeCompoundTag(toTag());
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompoundTag toTag() {
        return DataFixUtils.make(super.toTag(), tag -> {
            CompoundTag stateTag = new CompoundTag();
            for (Property<?> property : state.getProperties()) {
                Property<Comparable> unsafe = (Property<Comparable>) property;
                stateTag.putString(property.getName(), unsafe.getName(state.get(property)));
            }
            tag.put("State", stateTag);

            if (entity != null) {
                tag.put("EntityData", entity.toTag(new CompoundTag()));
            }
        });
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        super.onAdded(bus);
        if (entity instanceof Tickable) {
            bus.addContextlessListener(this, PartTickEvent.class, ((Tickable) entity)::tick);
        }
        bus.addListener(
                this, NeighbourUpdateEvent.class,
                event -> this.setMultipartState(multipart.getStateForNeighborUpdate(this, getOffset(getPos(), event.pos), event.pos))
        );
        updateProperties();
    }

    private static Direction getOffset(BlockPos a, BlockPos b) {
        BlockPos pos = a.subtract(b);
        return Direction.fromVector(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public VoxelShape getShape() {
        return multipart.getOutlineShape(this);
    }

    @Override
    public VoxelShape getCollisionShape() {
        return multipart.getCollisionShape(this);
    }

    @Override
    public VoxelShape getDynamicShape(float partialTicks) {
        return multipart.getDynamicShape(this, partialTicks);
    }

    @Override
    public boolean onActivate(PlayerEntity player, Hand hand, BlockHitResult hit) {
        return multipart.use(this, player, hand, hit);
    }

    @Nullable
    @Override
    public PartModelKey getModelKey() {
        return multipart.getModelKey(this);
    }

    @Override
    public boolean canOverlapWith(AbstractPart other) {
        if (other instanceof MultipartView) {
            return multipart.canOverlapWith(this, (MultipartView) other);
        }
        return multipart.canOverlapWith(this, other);
    }

    @Override
    public ItemStack getPickStack() {
        return multipart.getPickStack(this);
    }

    // implements MultipartView.Mutable

    @Override
    public World getWorld() {
        return holder.getContainer().getMultipartWorld();
    }

    @Override
    public BlockPos getPos() {
        return holder.getContainer().getMultipartPos();
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
        MultipartState previous = this.state;
        this.state = state;
        if (previous != state) {
            updateProperties();
        }
    }

    private void updateProperties() {
        MultipartPropertyContainer props = this.holder.getContainer().getProperties();
        props.setValue(this, MultipartProperties.CAN_EMIT_REDSTONE, multipart.canEmitRedstone(this));
        for (Direction direction : Direction.values()) {
            OptionalInt strong = multipart.getStrongRedstonePower(this, direction);
            if (strong.isPresent()) {
                props.setValue(this, MultipartProperties.getStrongRedstonePower(direction), strong.getAsInt());
            } else {
                props.clearValue(this, MultipartProperties.getStrongRedstonePower(direction));
            }

            OptionalInt weak = multipart.getWeakRedstonePower(this, direction);
            if (weak.isPresent()) {
                props.setValue(this, MultipartProperties.getWeakRedstonePower(direction), weak.getAsInt());
            } else {
                props.clearValue(this, MultipartProperties.getWeakRedstonePower(direction));
            }
        }
    }
}

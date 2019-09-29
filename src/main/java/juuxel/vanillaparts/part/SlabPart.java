package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartEventBus;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import alexiil.mc.lib.net.IMsgWriteCtx;
import alexiil.mc.lib.net.NetByteBuf;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import juuxel.vanillaparts.part.model.StaticVanillaModelKey;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.shape.VoxelShape;

public class SlabPart extends VanillaPart {
    private static final ImmutableMap<BlockHalf, VoxelShape> SHAPES;
    private final SlabBlock block;
    private final BlockHalf half;

    static {
        SHAPES = ImmutableMap.of(
                BlockHalf.BOTTOM, Block.createCuboidShape(0, 0, 0, 16, 8, 16),
                BlockHalf.TOP, Block.createCuboidShape(0, 8, 0, 16, 16, 16)
        );
    }

    public SlabPart(PartDefinition definition, MultipartHolder holder, SlabBlock block, BlockHalf half) {
        super(definition, holder);
        this.block = block;
        this.half = half;
    }

    public SlabPart(PartDefinition definition, MultipartHolder holder, SlabBlock block, CompoundTag tag) {
        this(definition, holder, block, tag.getBoolean("IsTop"));
    }

    public SlabPart(PartDefinition definition, MultipartHolder holder, SlabBlock block, boolean top) {
        super(definition, holder);
        this.block = block;
        this.half = top ? BlockHalf.TOP : BlockHalf.BOTTOM;
    }

    @Override
    public VoxelShape getShape() {
        return SHAPES.get(half);
    }

    @Override
    public PartModelKey getModelKey() {
        return new StaticVanillaModelKey(getVanillaState());
    }

    @Override
    public ItemStack getPickStack() {
        return new ItemStack(block);
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
    }

    @Override
    public BlockState getVanillaState() {
        return block.getDefaultState().with(SlabBlock.TYPE, half == BlockHalf.TOP ? SlabType.TOP : SlabType.BOTTOM);
    }

    @Override
    public CompoundTag toTag() {
        return DataFixUtils.make(new CompoundTag(), tag -> {
            tag.putBoolean("IsTop", half == BlockHalf.TOP);
        });
    }

    @Override
    public void writeCreationData(NetByteBuf buffer, IMsgWriteCtx ctx) {
        super.writeCreationData(buffer, ctx);
        buffer.writeBoolean(half == BlockHalf.TOP);
    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartEventBus;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.property.MultipartProperties;
import alexiil.mc.lib.multipart.api.property.MultipartPropertyContainer;
import alexiil.mc.lib.net.IMsgWriteCtx;
import alexiil.mc.lib.net.NetByteBuf;
import juuxel.blockstoparts.api.category.CategorySet;
import juuxel.vanillaparts.mixin.LeverBlockAccessor;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;

// TODO: Levers power blocks through other parts
public class LeverPart extends WallMountedRedstonePart {
    public LeverPart(PartDefinition definition, MultipartHolder holder, WallMountLocation face, Direction facing, boolean powered) {
        super(definition, holder, face, facing, powered);
    }

    public LeverPart(PartDefinition definition, MultipartHolder holder, NbtCompound tag) {
        this(definition, holder, readFace(tag.getInt("Face")), Direction.byId(tag.getInt("Facing")), tag.getBoolean("Powered"));
    }

    public LeverPart(PartDefinition definition, MultipartHolder holder, NetByteBuf buf) {
        this(definition, holder, readFace(buf.readByte()), Direction.byId(buf.readByte()), buf.readBoolean());
    }

    @Override
    public NbtCompound toTag() {
        return Util.with(super.toTag(), tag -> {
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
    public BlockState getBlockState() {
        return Blocks.LEVER.getDefaultState()
                .with(LeverBlock.FACE, face)
                .with(LeverBlock.FACING, facing)
                .with(LeverBlock.POWERED, powered);
    }

    @Override
    public ActionResult onUse(PlayerEntity player, Hand hand, BlockHitResult hit) {
        powered = !powered;
        updateRedstoneLevels();
        if (player.world.isClient) {
            if (powered) {
                LeverBlockAccessor.callSpawnParticles(getBlockState(), player.world, hit.getBlockPos(), 1f);
            }
        } else {
            float pitch = powered ? 0.6f : 0.5f;
            player.world.playSound(null, hit.getBlockPos(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, pitch);
        }
        updateListeners();
        return ActionResult.SUCCESS;
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        super.onAdded(bus);
        updateRedstoneLevels();
        MultipartPropertyContainer props = this.holder.getContainer().getProperties();
        props.setValue(this, MultipartProperties.CAN_EMIT_REDSTONE, true);
    }

    private static WallMountLocation readFace(int i) {
        return Util.safeGet(WallMountLocation.values(), i);
    }

    @Override
    protected void addCategories(CategorySet.Builder builder) {
        builder.add(VpCategories.LEVERS);
        builder.add(VpCategories.REDSTONE_COMPONENTS);
    }
}

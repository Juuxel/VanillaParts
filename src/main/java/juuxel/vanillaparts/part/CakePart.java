/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import alexiil.mc.lib.net.IMsgReadCtx;
import alexiil.mc.lib.net.IMsgWriteCtx;
import alexiil.mc.lib.net.InvalidInputDataException;
import alexiil.mc.lib.net.NetByteBuf;
import com.mojang.datafixers.DataFixUtils;
import juuxel.vanillaparts.part.model.DynamicVanillaModelKey;
import juuxel.vanillaparts.util.NbtKeys;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class CakePart extends VanillaPart {
    private static final int MAX_BITES = CakeBlock.BITES.getValues().stream()
            .max(Comparator.naturalOrder())
            .orElseThrow(() -> new RuntimeException("Could not get max value of Properties.BITES!"));

    private int bites;

    public CakePart(PartDefinition definition, MultipartHolder holder, int bites) {
        super(definition, holder);
        this.bites = bites;
    }

    public CakePart(PartDefinition definition, MultipartHolder holder) {
        this(definition, holder, 0);
    }

    public CakePart(PartDefinition definition, MultipartHolder holder, CompoundTag tag) {
        this(definition, holder, tag.getInt(NbtKeys.BITES));
    }

    public CakePart(PartDefinition definition, MultipartHolder holder, NetByteBuf buf) {
        this(definition, holder, buf.readByte());
    }

    @Override
    public BlockState getVanillaState() {
        return Blocks.CAKE.getDefaultState().with(CakeBlock.BITES, bites);
    }

    @Override
    public VoxelShape getShape() {
        return getVanillaState().getOutlineShape(getWorld(), getPos());
    }

    @Override
    public ActionResult onUse(PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.canConsume(false)) {
            player.incrementStat(Stats.EAT_CAKE_SLICE);
            player.getHungerManager().add(2, 0.1f);

            if (bites + 1 > MAX_BITES) {
                this.holder.remove();
            } else {
                bites++;
                updateListeners();
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public PartModelKey getModelKey() {
        return new DynamicVanillaModelKey(this);
    }

    @Override
    public CompoundTag toTag() {
        return DataFixUtils.make(super.toTag(), tag -> tag.putInt(NbtKeys.BITES, bites));
    }

    @Override
    public void writeCreationData(NetByteBuf buffer, IMsgWriteCtx ctx) {
        buffer.writeByte((byte) bites);
    }

    @Override
    public void readRenderData(NetByteBuf buffer, IMsgReadCtx ctx) throws InvalidInputDataException {
        super.readRenderData(buffer, ctx);
    }
}

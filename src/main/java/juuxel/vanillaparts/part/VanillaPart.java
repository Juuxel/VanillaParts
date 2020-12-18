/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.*;
import alexiil.mc.lib.multipart.api.event.NeighbourUpdateEvent;
import juuxel.vanillaparts.event.ClientNeighbourUpdateEvent;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public abstract class VanillaPart extends AbstractPart {
    public VanillaPart(PartDefinition definition, MultipartHolder holder) {
        super(definition, holder);
    }

    public abstract BlockState getVanillaState();

    protected final World getWorld() {
        return this.holder.getContainer().getMultipartWorld();
    }

    protected final BlockPos getPos() {
        return this.holder.getContainer().getMultipartPos();
    }

    protected final void removeAndDrop() {
        DefaultedList<ItemStack> stacks = DefaultedList.of();
        addDrops(stacks);
        MultipartContainer container = this.holder.getContainer();
        World world = container.getMultipartWorld();
        BlockPos pos = container.getMultipartPos();
        ItemScatterer.spawn(world, pos, stacks);
        world.syncWorldEvent(2001, pos, Block.getRawIdFromState(getVanillaState()));
        this.holder.remove();
    }

    protected final void updateListeners() {
        BlockPos pos = getPos();
        BlockState multipartState = getWorld().getBlockState(pos);
        getWorld().updateListeners(getPos(), multipartState, multipartState, 3);
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        bus.addListener(this, NeighbourUpdateEvent.class, event -> {
            MultipartContainer container = this.holder.getContainer();
            World world = container.getMultipartWorld();
            BlockPos pos = container.getMultipartPos();
            if (!getVanillaState().canPlaceAt(world, pos)) {
                this.removeAndDrop();
            }
            onNeighborUpdate(event.pos);
        });
        bus.addListener(this, ClientNeighbourUpdateEvent.class, event -> {
            onNeighborUpdate(event.pos);
        });
    }

    @Override
    public ItemStack getPickStack() {
        return new ItemStack(getVanillaState().getBlock());
    }

    @Override
    public void addDrops(ItemDropTarget target, LootContext context) {
        target.dropAll(getVanillaState().getDroppedStacks(Util.toBlockLootContext(context)));
    }

    /**
     * Called on both client and server neighbour update.
     */
    protected void onNeighborUpdate(BlockPos neighborPos) {}

    @Override
    public VoxelShape getCullingShape() {
        return getOutlineShape();
    }

    @Override
    protected void spawnBreakParticles() {
        spawnBreakParticles(getVanillaState());
    }

    @Override
    public float calculateBreakingDelta(PlayerEntity player) {
        return calculateBreakingDelta(player, getVanillaState());
    }

    @Override
    protected void playBreakSound() {
        playBreakSound(getVanillaState());
    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.*;
import alexiil.mc.lib.multipart.api.event.NeighbourUpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
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
        world.playLevelEvent(2001, pos, Block.getRawIdFromState(getVanillaState()));
        this.holder.remove();
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        System.out.println("sided? 1");
        bus.addListener(this, NeighbourUpdateEvent.class, event -> {
            System.out.println("sided? 2");
            MultipartContainer container = this.holder.getContainer();
            World world = container.getMultipartWorld();
            BlockPos pos = container.getMultipartPos();
            if (!getVanillaState().canPlaceAt(world, pos)) {
                this.removeAndDrop();
            }
            onNeighborUpdate(event.pos);
        });
    }

    @Override
    public ItemStack getPickStack() {
        return new ItemStack(getVanillaState().getBlock());
    }

    protected void onNeighborUpdate(BlockPos neighborPos) {}
}

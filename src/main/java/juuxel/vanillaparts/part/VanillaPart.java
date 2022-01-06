/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartEventBus;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.event.NeighbourStateUpdateEvent;
import juuxel.blockstoparts.api.category.CategorySet;
import juuxel.blockstoparts.api.part.BasePart;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public abstract class VanillaPart extends BasePart {
    private final CategorySet categories;

    public VanillaPart(PartDefinition definition, MultipartHolder holder) {
        super(definition, holder);
        this.categories = Util.with(CategorySet.builder(), this::addCategories).build();
    }

    protected final void updateListeners() {
        BlockPos pos = getPos();
        BlockState multipartState = getWorld().getBlockState(pos);
        getWorld().updateListeners(getPos(), multipartState, multipartState, 3);
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        bus.addListener(this, NeighbourStateUpdateEvent.class, event -> {
            if (!getWorld().isClient) {
                MultipartContainer container = this.holder.getContainer();
                World world = container.getMultipartWorld();
                BlockPos pos = container.getMultipartPos();
                if (!getBlockState().canPlaceAt(world, pos)) {
                    breakPart();
                }
            }

            onNeighborUpdate(event.pos);
        });
    }

    /**
     * Called on both client and server neighbour update.
     */
    protected void onNeighborUpdate(BlockPos neighborPos) {}

    @Override
    public VoxelShape getCullingShape() {
        return getOutlineShape();
    }

    protected abstract void addCategories(CategorySet.Builder builder);

    @Override
    public final CategorySet getCategories() {
        return categories;
    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.*;
import alexiil.mc.lib.multipart.api.event.NeighbourUpdateEvent;
import juuxel.blockstoparts.part.BasePart;
import juuxel.vanillaparts.event.ClientNeighbourUpdateEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class VanillaPart extends BasePart {
    public VanillaPart(PartDefinition definition, MultipartHolder holder) {
        super(definition, holder);
    }

    @Override
    public void onAdded(MultipartEventBus bus) {
        bus.addListener(this, NeighbourUpdateEvent.class, event -> {
            MultipartContainer container = this.holder.getContainer();
            World world = container.getMultipartWorld();
            BlockPos pos = container.getMultipartPos();
            if (!getBlockState().canPlaceAt(world, pos)) {
                this.removeAndDrop();
            }
            onNeighborUpdate(event.pos);
        });
        bus.addListener(this, ClientNeighbourUpdateEvent.class, event -> {
            onNeighborUpdate(event.pos);
        });
    }

    /**
     * Called on both client and server neighbour update.
     */
    protected void onNeighborUpdate(BlockPos neighborPos) {}
}

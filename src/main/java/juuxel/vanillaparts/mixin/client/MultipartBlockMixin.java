/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin.client;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import alexiil.mc.lib.multipart.impl.MultipartBlock;
import juuxel.vanillaparts.event.ClientNeighbourUpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultipartBlock.class)
public class MultipartBlockMixin extends Block {
    public MultipartBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction side, BlockState neighbourState, IWorld world, BlockPos pos, BlockPos neighbourPos) {
        if (world.isClient() && world instanceof World) {
            MultipartContainer container = MultipartUtil.get((World) world, pos);
            if (container != null) {
                container.fireEvent(new ClientNeighbourUpdateEvent(neighbourPos));
            }
        }
        return super.getStateForNeighborUpdate(state, side, neighbourState, world, pos, neighbourPos);
    }
}

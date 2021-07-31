/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import juuxel.vanillaparts.lib.Exclusions;
import juuxel.vanillaparts.part.TorchPart;
import juuxel.vanillaparts.part.VpParts;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(WallTorchBlock.class)
abstract class WallTorchBlockMixin extends TorchBlockMixin {
    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        if (Exclusions.isExcluded(state)) return null;
        MultipartContainer.MultipartCreator creator = null;

        if ((Object) this == Blocks.WALL_TORCH) {
            creator = holder -> new TorchPart(VpParts.TORCH, holder, Blocks.TORCH, Blocks.WALL_TORCH, TorchPart.Facing.of(state.get(WallTorchBlock.FACING)));
        } else if ((Object) this == Blocks.SOUL_WALL_TORCH) {
            creator = holder -> new TorchPart(VpParts.SOUL_TORCH, holder, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH, TorchPart.Facing.of(state.get(WallTorchBlock.FACING)));
        }

        return creator != null ? Collections.singletonList(creator) : null;
    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.vanillaparts.lib.Exclusions;
import juuxel.vanillaparts.part.TorchPart;
import juuxel.vanillaparts.part.VpParts;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(TorchBlock.class)
abstract class TorchBlockMixin implements NativeMultipart {
    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        if (Exclusions.isExcluded(state)) return null;
        MultipartContainer.MultipartCreator creator = null;

        if ((Object) this == Blocks.TORCH) {
            creator = holder -> new TorchPart(VpParts.TORCH, holder, Blocks.TORCH, Blocks.WALL_TORCH, TorchPart.Facing.GROUND);
        } else if ((Object) this == Blocks.SOUL_TORCH) {
            creator = holder -> new TorchPart(VpParts.SOUL_TORCH, holder, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH, TorchPart.Facing.GROUND);
        }

        return creator != null ? Collections.singletonList(creator) : null;
    }
}

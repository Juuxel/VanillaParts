/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.vanillaparts.lib.Exclusions;
import juuxel.vanillaparts.part.CarpetPart;
import juuxel.vanillaparts.part.VpParts;
import net.minecraft.block.BlockState;
import net.minecraft.block.DyedCarpetBlock;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.List;

@Mixin(DyedCarpetBlock.class)
abstract class DyedCarpetBlockMixin implements NativeMultipart {
    @Shadow
    public abstract DyeColor getDyeColor();

    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        if (Exclusions.isExcluded(state)) return null;
        DyeColor color = getDyeColor();
        return Collections.singletonList(holder -> new CarpetPart(VpParts.CARPET_PARTS.get(color), holder, color));
    }
}

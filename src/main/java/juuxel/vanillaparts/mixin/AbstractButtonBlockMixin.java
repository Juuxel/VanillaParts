/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import juuxel.vanillaparts.lib.Exclusions;
import juuxel.vanillaparts.part.ButtonPart;
import juuxel.vanillaparts.part.VpParts;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

@Mixin(AbstractButtonBlock.class)
abstract class AbstractButtonBlockMixin extends WallMountedBlock implements NativeMultipart {
    private AbstractButtonBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
        if (Exclusions.isExcluded(state)) return null;
        return Collections.singletonList(holder -> new ButtonPart(VpParts.BUTTON, holder, this, state.get(FACE), state.get(FACING)));
    }
}

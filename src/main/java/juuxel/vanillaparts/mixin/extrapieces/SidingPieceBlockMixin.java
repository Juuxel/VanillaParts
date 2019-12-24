/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin.extrapieces;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.NativeMultipart;
//import com.shnupbups.extrapieces.blocks.SidingPieceBlock;
//import com.shnupbups.extrapieces.register.ModProperties;
import juuxel.vanillaparts.compat.extrapieces.ExtraPiecesCompat;
import juuxel.vanillaparts.compat.extrapieces.SidingPart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;

//@Mixin(SidingPieceBlock.class)
public abstract class SidingPieceBlockMixin extends Block implements NativeMultipart {
    public SidingPieceBlockMixin(Settings settings) {
        super(settings);
    }

//    @Override
//    public List<MultipartContainer.MultipartCreator> getMultipartConversion(World world, BlockPos pos, BlockState state) {
//        if (state.get(SidingPieceBlock.TYPE) == ModProperties.SidingType.DOUBLE) return null;
//        return Collections.singletonList(holder -> new SidingPart(ExtraPiecesCompat.sidingParts.get(this), holder, this, state.get(SidingPieceBlock.FACING_HORIZONTAL)));
//    }
}

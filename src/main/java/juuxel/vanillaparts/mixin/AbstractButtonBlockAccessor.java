/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractButtonBlock.class)
public interface AbstractButtonBlockAccessor {
    @Accessor
    boolean isWooden();

    @Invoker
    void callPlayClickSound(/*@Nullable*/ PlayerEntity player, IWorld world, BlockPos pos, boolean powered);
}

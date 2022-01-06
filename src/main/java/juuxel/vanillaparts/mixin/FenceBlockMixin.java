/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import juuxel.vanillaparts.part.FencePart;
import juuxel.vanillaparts.util.FenceExtensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FenceBlock.class)
abstract class FenceBlockMixin extends HorizontalConnectingBlock implements FenceExtensions {
    @Shadow
    protected abstract boolean canConnectToFence(BlockState state);

    private FenceBlockMixin(float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, Settings settings) {
        super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
    }

    @Override
    public boolean vanillaParts_canConnectToMultiparts(WorldAccess world, BlockPos neighborPos, Direction sideOfOther) {
        MultipartContainer container = MultipartUtil.get(world, neighborPos);
        return container != null && !container.getAllParts(part -> part instanceof FencePart fence && canConnectToFence(fence.getBlockState()) && !((FencePart) part).isBlocked(sideOfOther)).isEmpty();
    }

    @Redirect(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FenceBlock;canConnect(Lnet/minecraft/block/BlockState;ZLnet/minecraft/util/math/Direction;)Z"))
    private boolean redirectCanConnect_getStateForNeighborUpdate(FenceBlock self, BlockState neighborState1, boolean neighborIsFullSquare, Direction sideOfOther, BlockState state, Direction direction, BlockState neighborState2, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return self.canConnect(neighborState1, neighborIsFullSquare, sideOfOther) || vanillaParts_canConnectToMultiparts(world, neighborPos, sideOfOther);
    }

    @Redirect(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FenceBlock;canConnect(Lnet/minecraft/block/BlockState;ZLnet/minecraft/util/math/Direction;)Z"))
    private boolean redirectCanConnect_getPlacementState(FenceBlock self, BlockState state, boolean neighborIsFullSquare, Direction sideOfOther, ItemPlacementContext ctx) {
        return self.canConnect(state, neighborIsFullSquare, sideOfOther) || vanillaParts_canConnectToMultiparts(ctx.getWorld(), ctx.getBlockPos().offset(sideOfOther.getOpposite()), sideOfOther);
    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import juuxel.vanillaparts.part.FencePart;
import juuxel.vanillaparts.util.FenceExtensions;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FenceBlock.class)
abstract class FenceBlockMixin extends HorizontalConnectingBlock implements FenceExtensions {
    private FenceBlockMixin(float f, float g, float h, float i, float j, Settings settings) {
        super(f, g, h, i, j, settings);
    }

    @Override
    public boolean vanillaParts_canConnect(WorldAccess world, BlockPos neighborPos, Direction sideOfOther) {
        MultipartContainer container = MultipartUtil.get(world, neighborPos);
        return container != null && !container.getAllParts(part -> part instanceof FencePart && ((FencePart) part).getBlock().getDefaultState().getMaterial() == this.material && !((FencePart) part).isBlocked(sideOfOther)).isEmpty();
    }

    @Redirect(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FenceBlock;canConnect(Lnet/minecraft/block/BlockState;ZLnet/minecraft/util/math/Direction;)Z"))
    private boolean redirectCanConnect_getStateForNeighborUpdate(FenceBlock self, BlockState state, boolean b, Direction sideOfOther, BlockState bs1, Direction d1, BlockState bs2, WorldAccess world, BlockPos pos1, BlockPos pos2) {
        return self.canConnect(state, b, sideOfOther) || vanillaParts_canConnect(world, pos2, sideOfOther);
    }

    @Redirect(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FenceBlock;canConnect(Lnet/minecraft/block/BlockState;ZLnet/minecraft/util/math/Direction;)Z"))
    private boolean redirectCanConnect_getPlacementState(FenceBlock self, BlockState state, boolean b, Direction sideOfOther, ItemPlacementContext ctx) {
        return self.canConnect(state, b, sideOfOther) || vanillaParts_canConnect(ctx.getWorld(), ctx.getBlockPos().offset(sideOfOther.getOpposite()), sideOfOther);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block source, BlockPos neighborPos, boolean flag) {
        super.neighborUpdate(state, world, pos, source, neighborPos, flag);
        replace(state, state.getStateForNeighborUpdate(Util.compare(pos, neighborPos), world.getBlockState(neighborPos), world, pos, neighborPos), world, pos, 3);
    }
}

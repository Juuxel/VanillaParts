/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.compat.extrapieces;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import com.shnupbups.extrapieces.blocks.SidingPieceBlock;
import juuxel.vanillaparts.MultipartItemTweak;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static juuxel.vanillaparts.MultipartItemTweak.isMissingContainer;

public enum SidingItemTweak implements MultipartItemTweak.Extension {
    INSTANCE;

    private static final Logger LOGGER = LogManager.getLogger();

    public boolean isSiding(Block block) {
        return block instanceof SidingPieceBlock;
    }

    @Override
    public MultipartContainer.PartOffer handle(Block block, PlayerEntity player, World world, Hand hand, BlockHitResult hit, BlockPos pos) {
        if (block instanceof SidingPieceBlock) {
            if (!ExtraPiecesCompat.sidingParts.containsKey(block)) {
                LOGGER.warn("Siding " + block + " is missing a part! Report this to Vanilla Parts!");
                return null;
            }

            if (world.getBlockState(hit.getBlockPos()).getBlock() == block) return null;
            boolean missingAtHit = isMissingContainer(world, hit.getBlockPos());
            boolean missingAtOffset = isMissingContainer(world, pos);
            if (missingAtHit && missingAtOffset) return null;
            Direction hitSide = hit.getSide();
//            System.out.println("hitside: " + hitSide);
//            System.out.println("hitaxis: " + hitSide.getAxis());
//            System.out.println("hit: " + hit.getPos());

            Direction facing = hitSide.getOpposite();
            if (hitSide.getAxis().isVertical()) {
                facing = player.getHorizontalFacing().getOpposite();
                if (facing.getAxis() == Direction.Axis.X) {
                    if (hit.getPos().z - pos.getZ() > 0.5) {
                        facing = Direction.NORTH;
                    } else {
                        facing = Direction.SOUTH;
                    }
                } else {
                    if (hit.getPos().x - pos.getX() > 0.5) {
                        facing = Direction.WEST;
                    } else {
                        facing = Direction.EAST;
                    }
                }
            } else {
                double axisOffset = hit.getPos().getComponentAlongAxis(hitSide.getAxis()) -
                        (hitSide.getAxis() == Direction.Axis.X ? pos.getX() : pos.getZ());

                if (axisOffset == 0.0 || axisOffset == 1.0 || missingAtHit) {
                    facing = hitSide;
                    if (missingAtOffset) return null;
                } else {
                    pos = pos.offset(hitSide.getOpposite());
                }
            }

            Direction finalFacing = facing; // thanks java
            return MultipartUtil.offerNewPart(
                    world, pos,
                    holder -> new SidingPart(ExtraPiecesCompat.sidingParts.get(block), holder, block, finalFacing)
            );
        }

        return null;
    }
}

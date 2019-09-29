package juuxel.vanillaparts;

import alexiil.mc.lib.multipart.api.*;
import juuxel.vanillaparts.block.VBlocks;
import juuxel.vanillaparts.part.*;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.function.BiFunction;

enum MultipartItemTweak implements UseBlockCallback {
    INSTANCE;

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockItem bi = (BlockItem) item;
            Block block = bi.getBlock();
            BlockPos pos = hit.getBlockPos().offset(hit.getSide());
            MultipartContainer.PartOffer offer = null;

            if (isMissingContainer(world, pos) && !(block instanceof SlabBlock)) // slabs do custom checking
                return ActionResult.PASS; // Revert to vanilla placement

            if (block instanceof CarpetBlock) {
                offer = handleCarpets(world, pos, block);
            } else if (block == Blocks.TORCH) {
                offer = handleTorches(player, world, hand, hit, pos);
            } else if (block == VBlocks.CRAFTING_SLAB) {
                offer = handleCraftingSlabs(player, world, hand, hit, pos, block);
            } else if (block instanceof SlabBlock) {
                offer = handleSlabs(player, world, hand, hit, pos, block);
            } else if (block == Blocks.LEVER) {
                offer = handleWallMounted(player, world, hand, hit, pos, block, (holder, face, facing) -> new LeverPart(VPartDefinitions.LEVER, holder, face, facing, false));
            } else if (block instanceof AbstractButtonBlock) {
                offer = handleWallMounted(player, world, hand, hit, pos, block, (holder, face, facing) -> new ButtonPart(VPartDefinitions.BUTTON_PARTS.get(block), holder, block, face, facing));
            } else {
                return ActionResult.PASS;
            }

            if (offer != null) {
                if (!world.isClient) {
                    offer.apply();
                    if (!player.abilities.creativeMode) {
                        stack.decrement(1);
                    }
                }

                BlockSoundGroup sounds = block.getDefaultState().getSoundGroup();
                world.playSound(player, pos, sounds.getPlaceSound(), SoundCategory.BLOCKS, (sounds.getVolume() + 1f) / 2f, sounds.getPitch() * 0.8f);

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    private boolean isMissingContainer(World world, BlockPos pos) {
        return !(world.getBlockState(pos).getBlock() instanceof NativeMultipart) && MultipartUtil.get(world, pos) == null;
    }

    private MultipartContainer.PartOffer handleCarpets(World world, BlockPos pos, Block block) {
        if (!(block.getDefaultState().canPlaceAt(world, pos))) {
            return null;
        }

        DyeColor color = ((CarpetBlock) block).getColor();
        return MultipartUtil.offerNewPart(
                world, pos,
                holder -> new CarpetPart(VPartDefinitions.CARPET_PARTS.get(color), holder, color)
        );
    }

    private MultipartContainer.PartOffer handleTorches(PlayerEntity player, World world, Hand hand, BlockHitResult hit, BlockPos pos) {
        if (hit.getSide().getAxis().isHorizontal()) {
            ItemPlacementContext ctx = new ItemPlacementContext(new ItemUsageContext(player, hand, hit));
            BlockState torchState = Blocks.WALL_TORCH.getPlacementState(ctx);
            if (torchState == null || !torchState.canPlaceAt(world, pos)) {
                return null;
            }
        } else if (!Blocks.TORCH.getDefaultState().canPlaceAt(world, pos)) {
            return null;
        }

        TorchPart.Facing facing = TorchPart.Facing.of(hit.getSide());
        return MultipartUtil.offerNewPart(
                world, pos, holder -> new TorchPart(VPartDefinitions.TORCH, holder, facing)
        );
    }

    private MultipartContainer.PartOffer handleSlabs(PlayerEntity player, World world, Hand hand, BlockHitResult hit, BlockPos pos, Block block) {
        // TODO: Improve slab stacking
        if (!VPartDefinitions.SLAB_PARTS.containsKey(block)) return null;
        return handleAnySlabs(player, world, hand, hit, pos, block, (holder, top) -> new SlabPart(VPartDefinitions.SLAB_PARTS.get(block), holder, (SlabBlock) block, top));
    }

    private MultipartContainer.PartOffer handleCraftingSlabs(PlayerEntity player, World world, Hand hand, BlockHitResult hit, BlockPos pos, Block block) {
        return handleAnySlabs(player, world, hand, hit, pos, block, (holder, top) -> new CraftingSlabPart(VPartDefinitions.CRAFTING_SLAB, holder, (SlabBlock) block, top));
    }

    private MultipartContainer.PartOffer handleAnySlabs(PlayerEntity player, World world, Hand hand, BlockHitResult hit, BlockPos pos, Block block, BiFunction<MultipartHolder, Boolean, AbstractPart> factory) {
        BlockState hitState = world.getBlockState(hit.getBlockPos());
        if (block == hitState.getBlock() && hit.getSide().getAxis().isVertical()) {
            Direction side = hit.getSide();
            if (side == Direction.UP && hitState.get(SlabBlock.TYPE) == SlabType.BOTTOM) return null;
            if (side == Direction.DOWN && hitState.get(SlabBlock.TYPE) == SlabType.TOP) return null;
        }

        BlockPos originalPos = pos.offset(hit.getSide().getOpposite());
        MultipartContainer.PartOffer offer = null;
        if (!isMissingContainer(world, originalPos) && hit.getSide().getAxis().isVertical()) {
            offer = MultipartUtil.offerNewPart(
                    world, originalPos, holder -> factory.apply(holder, hit.getSide() == Direction.UP)
            );
        }

        if (offer == null && !isMissingContainer(world, pos)) {
            BlockState placementState = block.getPlacementState(new ItemPlacementContext(new ItemUsageContext(player, hand, hit)));
            if (placementState == null || placementState.get(SlabBlock.TYPE) == SlabType.DOUBLE)
                return null;

            offer = MultipartUtil.offerNewPart(
                    world, pos, holder -> factory.apply(holder, placementState.get(SlabBlock.TYPE) == SlabType.TOP)
            );
        }

        return offer;
    }

    private MultipartContainer.PartOffer handleWallMounted(PlayerEntity player, World world, Hand hand, BlockHitResult hit, BlockPos pos, Block block, WallMountedPartFactory factory) {
        ItemPlacementContext ctx = new ItemPlacementContext(new ItemUsageContext(player, hand, hit));
        Direction[] directions = ctx.getPlacementDirections();
        for (Direction direction : directions) {
            BlockState vanillaState;
            if (direction.getAxis().isVertical()) {
                vanillaState = block.getDefaultState()
                        .with(LeverBlock.FACE, direction == Direction.UP ? WallMountLocation.CEILING : WallMountLocation.FLOOR)
                        .with(LeverBlock.FACING, ctx.getPlayerFacing());
            } else {
                vanillaState = block.getDefaultState()
                        .with(LeverBlock.FACE, WallMountLocation.WALL)
                        .with(LeverBlock.FACING, direction.getOpposite());
            }

            if (vanillaState.canPlaceAt(world, pos)) {
                return MultipartUtil.offerNewPart(
                        world, pos,
                        holder -> factory.create(
                                holder,
                                vanillaState.get(LeverBlock.FACE),
                                vanillaState.get(LeverBlock.FACING)
                        )
                );
            }
        }

        return null;
    }

    @FunctionalInterface
    private interface WallMountedPartFactory {
        AbstractPart create(MultipartHolder holder, WallMountLocation face, Direction facing);
    }
}

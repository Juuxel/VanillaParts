package juuxel.vanillaparts;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import alexiil.mc.lib.multipart.api.PartDefinition;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import juuxel.vanillaparts.api.VanillaPartsInitializer;
import juuxel.vanillaparts.part.CarpetPart;
import juuxel.vanillaparts.part.SlabPart;
import juuxel.vanillaparts.part.StatelessPartFactory;
import juuxel.vanillaparts.part.TorchPart;
import juuxel.vanillaparts.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.List;

public final class VanillaParts implements ModInitializer {
    public static final ImmutableMap<DyeColor, PartDefinition> CARPET_PARTS;
    public static final ImmutableMap<DyeColor, Block> CARPETS;
    public static final PartDefinition TORCH = new PartDefinition(
            id("torch"), TorchPart::new,
            (definition, holder, buffer, ctx) -> new TorchPart(definition, holder, buffer.readByte())
    );
    public static final BiMap<Block, PartDefinition> SLAB_PARTS = HashBiMap.create();

    static {
        ImmutableMap.Builder<DyeColor, PartDefinition> partMapBuilder = ImmutableMap.builder();
        for (DyeColor color : DyeColor.values()) {
            StatelessPartFactory factory = (def, holder) -> new CarpetPart(def, holder, color);
            PartDefinition definition = new PartDefinition(id(color.asString() + "_carpet"), factory, factory);
            partMapBuilder.put(color, definition);
        }

        CARPET_PARTS = partMapBuilder.build();

        ImmutableMap.Builder<DyeColor, Block> carpetMapBuilder = ImmutableMap.builder();
        carpetMapBuilder
                .put(DyeColor.WHITE, Blocks.WHITE_CARPET)
                .put(DyeColor.ORANGE, Blocks.ORANGE_CARPET)
                .put(DyeColor.MAGENTA, Blocks.MAGENTA_CARPET)
                .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CARPET)
                .put(DyeColor.YELLOW, Blocks.YELLOW_CARPET)
                .put(DyeColor.LIME, Blocks.LIME_CARPET)
                .put(DyeColor.PINK, Blocks.PINK_CARPET)
                .put(DyeColor.GRAY, Blocks.GRAY_CARPET)
                .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CARPET)
                .put(DyeColor.CYAN, Blocks.CYAN_CARPET)
                .put(DyeColor.PURPLE, Blocks.PURPLE_CARPET)
                .put(DyeColor.BLUE, Blocks.BLUE_CARPET)
                .put(DyeColor.BROWN, Blocks.BROWN_CARPET)
                .put(DyeColor.GREEN, Blocks.GREEN_CARPET)
                .put(DyeColor.RED, Blocks.RED_CARPET)
                .put(DyeColor.BLACK, Blocks.BLACK_CARPET);

        CARPETS = carpetMapBuilder.build();
    }

    public static Identifier id(String path) {
        return new Identifier("vanilla_parts", path);
    }

    private static void register(PartDefinition def) {
        PartDefinition.PARTS.put(def.identifier, def);
    }

    @Override
    public void onInitialize() {
        // Register carpet parts
        for (PartDefinition def : CARPET_PARTS.values()) {
            register(def);
        }
        register(TORCH);

        // Load VP initializers
        List<VanillaPartsInitializer> initializers = FabricLoader.getInstance()
                .getEntrypoints("vanilla_parts", VanillaPartsInitializer.class);

        for (VanillaPartsInitializer initializer : initializers) {
            initializer.onCarpetPartInitialize();
        }

        // Register carpet and torch item tweak
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            ItemStack stack = player.getStackInHand(hand);
            Item item = stack.getItem();
            if (item instanceof BlockItem) {
                BlockItem bi = (BlockItem) item;
                Block block = bi.getBlock();
                BlockPos pos = hit.getBlockPos().offset(hit.getSide());
                MultipartContainer.PartOffer offer = null;
                if (block instanceof CarpetBlock) {
                    if (!(block.getDefaultState().canPlaceAt(world, pos))) {
                        return ActionResult.PASS;
                    }
                    boolean hasNative = world.getBlockState(pos).getBlock() instanceof NativeMultipart;
                    if (!hasNative && MultipartUtil.get(world, pos) == null) {
                        return ActionResult.PASS; // Revert to default placing
                    }

                    DyeColor color = ((CarpetBlock) block).getColor();
                    offer = MultipartUtil.offerNewPart(
                            world, pos,
                            holder -> new CarpetPart(CARPET_PARTS.get(color), holder, color)
                    );
                } else if (block == Blocks.TORCH) {
                    boolean hasNative = world.getBlockState(pos).getBlock() instanceof NativeMultipart;
                    if (!hasNative && MultipartUtil.get(world, pos) == null) {
                        return ActionResult.PASS; // Revert to default placing
                    } else if (hit.getSide().getAxis().isHorizontal()) {
                        ItemPlacementContext ctx = new ItemPlacementContext(new ItemUsageContext(player, hand, hit));
                        BlockState torchState = Blocks.WALL_TORCH.getPlacementState(ctx);
                        if (torchState == null || !torchState.canPlaceAt(world, pos)) {
                            return ActionResult.PASS;
                        }
                    } else if (!Blocks.TORCH.getDefaultState().canPlaceAt(world, pos)) {
                        return ActionResult.PASS;
                    }

                    TorchPart.Facing facing = TorchPart.Facing.of(hit.getSide());
                    offer = MultipartUtil.offerNewPart(
                            world, pos, holder -> new TorchPart(TORCH, holder, facing)
                    );
                } else if (block instanceof SlabBlock) {
                    // TODO: Improve slab stacking
                    if (!SLAB_PARTS.containsKey(block)) return ActionResult.PASS;
                    boolean hasNative = world.getBlockState(pos).getBlock() instanceof NativeMultipart;
                    if (!hasNative && MultipartUtil.get(world, pos) == null) {
                        return ActionResult.PASS; // Revert to default placing
                    }

                    BlockState placementState = block.getPlacementState(new ItemPlacementContext(new ItemUsageContext(player, hand, hit)));
                    if (placementState == null || placementState.get(SlabBlock.TYPE) == SlabType.DOUBLE)
                        return ActionResult.PASS;

                    offer = MultipartUtil.offerNewPart(
                            world, pos, holder -> new SlabPart(SLAB_PARTS.get(block), holder, (SlabBlock) block, placementState.get(SlabBlock.TYPE) == SlabType.TOP)
                    );
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
        });

        Util.visitRegistry(Registry.BLOCK, (id, block) -> {
            if (block instanceof SlabBlock) {
                PartDefinition def = new PartDefinition(
                        id(id.getNamespace() + "/" + id.getPath()),
                        (definition, holder, tag) -> new SlabPart(definition, holder, (SlabBlock) block, tag),
                        (definition, holder, buf, ctx) -> new SlabPart(definition, holder, (SlabBlock) block, buf.readBoolean())
                );
                register(def);
                SLAB_PARTS.put(block, def);
            }
        });
    }
}

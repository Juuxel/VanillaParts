package juuxel.carpetparts;

import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import alexiil.mc.lib.multipart.api.NativeMultipart;
import alexiil.mc.lib.multipart.api.PartDefinition;
import com.google.common.collect.ImmutableMap;
import juuxel.carpetparts.api.CarpetPartInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public final class CarpetParts implements ModInitializer {
    public static final ImmutableMap<DyeColor, PartDefinition> CARPET_PARTS;
    public static final ImmutableMap<DyeColor, Block> CARPETS;

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
        return new Identifier("carpet_parts", path);
    }

    @Override
    public void onInitialize() {
        // Register carpet parts
        for (PartDefinition def : CARPET_PARTS.values()) {
            PartDefinition.PARTS.put(def.identifier, def);
        }

        // Load carpet part initializers
        List<CarpetPartInitializer> initializers = FabricLoader.getInstance()
                .getEntrypoints("carpet_parts", CarpetPartInitializer.class);

        for (CarpetPartInitializer initializer : initializers) {
            initializer.onCarpetPartInitialize();
        }

        // Register carpet item tweak
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            ItemStack stack = player.getStackInHand(hand);
            Item item = stack.getItem();
            if (item instanceof BlockItem) {
                BlockItem bi = (BlockItem) item;
                Block block = bi.getBlock();
                if (block instanceof CarpetBlock) {
                    BlockPos pos = hit.getBlockPos().offset(hit.getSide());
                    boolean hasNative = world.getBlockState(pos).getBlock() instanceof NativeMultipart;
                    if (!hasNative && MultipartUtil.get(world, pos) == null) {
                        return ActionResult.PASS; // Revert to default placing
                    }

                    DyeColor color = ((CarpetBlock) block).getColor();
                    MultipartContainer.PartOffer offer = MultipartUtil.offerNewPart(
                            world, pos,
                            holder -> new CarpetPart(CARPET_PARTS.get(color), holder, color)
                    );

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
            }

            return ActionResult.PASS;
        });
    }
}

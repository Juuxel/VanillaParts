package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.PartDefinition;
import com.google.common.collect.ImmutableMap;
import juuxel.vanillaparts.VanillaParts;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public final class VPartDefinitions {
    // Block maps
    public static final ImmutableMap<DyeColor, Block> CARPETS;

    // Part maps
    public static final ImmutableMap<DyeColor, PartDefinition> CARPET_PARTS;
    public static final Map<Block, PartDefinition> SLAB_PARTS = new HashMap<>();
    public static final Map<Block, PartDefinition> BUTTON_PARTS = new HashMap<>();

    // Parts
    public static final PartDefinition TORCH = new PartDefinition(
            VanillaParts.id("torch"), TorchPart::new,
            (definition, holder, buffer, ctx) -> new TorchPart(definition, holder, buffer.readByte())
    );
    public static final PartDefinition LEVER = new PartDefinition(
            VanillaParts.id("lever"), LeverPart::new,
            ((definition, holder, buffer, ctx) -> new LeverPart(definition, holder, buffer))
    );

    private VPartDefinitions() {}

    private static void register(PartDefinition def) {
        PartDefinition.PARTS.put(def.identifier, def);
    }

    public static void init() {
        // Register parts
        for (PartDefinition def : CARPET_PARTS.values()) {
            register(def);
        }
        register(TORCH);
        register(LEVER);

        // Add slab and button parts
        Util.visitRegistry(Registry.BLOCK, (id, block) -> {
            if (block instanceof SlabBlock) {
                PartDefinition def = new PartDefinition(
                        VanillaParts.id(id.getNamespace() + "/" + id.getPath()),
                        (definition, holder, tag) -> new SlabPart(definition, holder, (SlabBlock) block, tag),
                        (definition, holder, buf, ctx) -> new SlabPart(definition, holder, (SlabBlock) block, buf.readBoolean())
                );
                register(def);
                SLAB_PARTS.put(block, def);
            } else if (block instanceof AbstractButtonBlock) {
                PartDefinition def = new PartDefinition(
                        VanillaParts.id(id.getNamespace() + "/" + id.getPath()),
                        (definition, holder, tag) -> new ButtonPart(definition, holder, block, tag),
                        (definition, holder, buf, ctx) -> new ButtonPart(definition, holder, block, buf)
                );
                register(def);
                BUTTON_PARTS.put(block, def);
            }
        });
    }

    static {
        ImmutableMap.Builder<DyeColor, PartDefinition> partMapBuilder = ImmutableMap.builder();
        for (DyeColor color : DyeColor.values()) {
            StatelessPartFactory factory = (def, holder) -> new CarpetPart(def, holder, color);
            PartDefinition definition = new PartDefinition(VanillaParts.id(color.asString() + "_carpet"), factory, factory);
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
}

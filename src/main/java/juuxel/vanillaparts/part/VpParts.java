/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.PartDefinition;
import com.google.common.collect.ImmutableMap;
import juuxel.vanillaparts.VanillaParts;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;

public final class VpParts {
    // Block maps
    public static final ImmutableMap<DyeColor, Block> CARPETS;

    // Part maps
    public static final ImmutableMap<DyeColor, PartDefinition> CARPET_PARTS;

    // Parts
    public static final PartDefinition TORCH = new PartDefinition(
        VanillaParts.id("torch"), TorchPart.fromNbt(Blocks.TORCH, Blocks.WALL_TORCH),
        TorchPart.fromBuf(Blocks.TORCH, Blocks.WALL_TORCH)
    );
    public static final PartDefinition SOUL_TORCH = new PartDefinition(
        VanillaParts.id("soul_torch"), TorchPart.fromNbt(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH),
        TorchPart.fromBuf(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH)
    );
    public static final PartDefinition LEVER = new PartDefinition(VanillaParts.id("lever"), LeverPart::fromNbt, LeverPart::fromBuf);
    public static final PartDefinition CAKE = new PartDefinition(VanillaParts.id("cake"), CakePart::fromNbt, CakePart::fromBuf);
    public static final PartDefinition FENCE = new PartDefinition(VanillaParts.id("fence"), FencePart::fromNbt, FencePart::fromBuf);
    public static final PartDefinition SLAB = new PartDefinition(VanillaParts.id("slab"), SlabPart::fromNbt, SlabPart::fromBuf);
    public static final PartDefinition BUTTON = new PartDefinition(VanillaParts.id("button"), ButtonPart::fromNbt, ButtonPart::fromBuf);

    private VpParts() {}

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
        register(CAKE);
        register(FENCE);
        register(SLAB);
        register(BUTTON);
        register(SOUL_TORCH);
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

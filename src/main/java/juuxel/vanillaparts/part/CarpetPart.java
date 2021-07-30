/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import juuxel.blockstoparts.api.category.CategorySet;
import juuxel.blockstoparts.api.model.StaticVanillaModelKey;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.DyeColor;
import net.minecraft.util.shape.VoxelShape;

public class CarpetPart extends VanillaPart {
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private final DyeColor color;

    public CarpetPart(PartDefinition definition, MultipartHolder holder, DyeColor color) {
        super(definition, holder);
        this.color = color;
    }

    @Override
    public VoxelShape getShape() {
        return SHAPE;
    }

    @Override
    public PartModelKey getModelKey() {
        return new StaticVanillaModelKey(getBlockState());
    }

    @Override
    public BlockState getBlockState() {
        return VpParts.CARPETS.get(color).getDefaultState();
    }

    @Override
    protected void addCategories(CategorySet.Builder builder) {
        builder.add(VpCategories.CARPETS);
        builder.overlap(VpCategories.FENCES);
    }
}

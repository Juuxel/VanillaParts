package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.AbstractPart;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import juuxel.vanillaparts.api.OverlappingPartRegistry;
import juuxel.vanillaparts.part.model.StaticVanillaModelKey;
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
        return new StaticVanillaModelKey(getVanillaState());
    }

    @Override
    public boolean canOverlapWith(AbstractPart other) {
        return OverlappingPartRegistry.canOverlapWithCarpets(other.definition);
    }

    @Override
    public BlockState getVanillaState() {
        return VPartDefinitions.CARPETS.get(color).getDefaultState();
    }
}

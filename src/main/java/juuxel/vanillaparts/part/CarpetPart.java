package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.AbstractPart;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import juuxel.vanillaparts.VanillaParts;
import juuxel.vanillaparts.api.OverlappingPartRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
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
        return new ModelKey(color);
    }

    @Override
    public ItemStack getPickStack() {
        return new ItemStack(VPartDefinitions.CARPETS.get(color));
    }

    @Override
    public boolean canOverlapWith(AbstractPart other) {
        return OverlappingPartRegistry.canOverlapWithCarpets(other.definition);
    }

    @Override
    public BlockState getVanillaState() {
        return VPartDefinitions.CARPETS.get(color).getDefaultState();
    }

    public static final class ModelKey extends PartModelKey {
        private final DyeColor color;

        private ModelKey(DyeColor color) {
            this.color = color;
        }

        public DyeColor getColor() {
            return color;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ModelKey modelKey = (ModelKey) o;
            return color == modelKey.color;
        }

        @Override
        public int hashCode() {
            return color.hashCode();
        }
    }
}

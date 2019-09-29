package juuxel.vanillaparts.part.model;

import net.minecraft.block.BlockState;

public class StaticVanillaModelKey extends VanillaModelKey {
    private final BlockState state;

    public StaticVanillaModelKey(BlockState state) {
        this.state = state;
    }

    @Override
    public BlockState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return state.equals(((StaticVanillaModelKey) o).state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }
}

package juuxel.vanillaparts.part.model;

import alexiil.mc.lib.multipart.api.render.PartModelKey;
import net.minecraft.block.BlockState;

public abstract class VanillaModelKey extends PartModelKey {
    public abstract BlockState getState();
}

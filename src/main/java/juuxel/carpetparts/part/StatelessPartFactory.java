package juuxel.carpetparts.part;

import alexiil.mc.lib.multipart.api.AbstractPart;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.net.IMsgReadCtx;
import alexiil.mc.lib.net.NetByteBuf;
import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface StatelessPartFactory extends PartDefinition.IPartNbtReader, PartDefinition.IPartNetLoader {
    AbstractPart createPart(PartDefinition def, MultipartHolder holder);

    @Override
    default AbstractPart readFromNbt(PartDefinition def, MultipartHolder holder, CompoundTag tag) {
        return createPart(def, holder);
    }

    @Override
    default AbstractPart loadFromBuffer(PartDefinition def, MultipartHolder holder, NetByteBuf buf, IMsgReadCtx ctx) {
        return createPart(def, holder);
    }
}

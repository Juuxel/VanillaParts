package juuxel.vanillaparts.impl;

import alexiil.mc.lib.multipart.api.AbstractPart;
import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import alexiil.mc.lib.net.IMsgReadCtx;
import alexiil.mc.lib.net.NetByteBuf;
import juuxel.vanillaparts.api.part.Multipart;
import juuxel.vanillaparts.api.part.MultipartState;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MultipartDefinition extends PartDefinition {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Multipart multipart;

    public MultipartDefinition(Identifier id, Multipart multipart) {
        super(id, new MultipartNbtReader(multipart), new MultipartBufLoader(multipart));
        this.multipart = multipart;
    }

    public Multipart getMultipart() {
        return multipart;
    }

    private static final class MultipartNbtReader implements IPartNbtReader {
        private final Multipart multipart;

        private MultipartNbtReader(Multipart multipart) {
            this.multipart = multipart;
        }

        @SuppressWarnings("unchecked")
        @Override
        public AbstractPart readFromNbt(PartDefinition definition, MultipartHolder holder, CompoundTag tag) {
            SimpleView view = new SimpleView();
            view.world = holder.getContainer().getMultipartWorld();
            view.pos = holder.getContainer().getMultipartPos();

            MultipartState state = multipart.getDefaultState();
            CompoundTag stateTag = tag.getCompound("State");
            for (String key : stateTag.getKeys()) {
                Property<? extends Comparable> property = multipart.getStateFactory().getProperty(key);
                if (property == null) {
                    LOGGER.warn("Found unknown property '{}' in serialized form of multipart {}", key, definition.identifier);
                    continue;
                }
                Optional<? extends Comparable> value = property.getValue(stateTag.getString(key));
                if (!value.isPresent()) {
                    LOGGER.warn("Found invalid value '{}' for property '{}' in serialized form of multipart {}", stateTag.getString(key), key, definition.identifier);
                    continue;
                }

                state = state.with((Property<Comparable>) property, value.get());
            }

            view.state = state;
            view.entity = multipart.createEntity(view);

            if (view.entity != null && tag.containsKey("EntityData", NbtType.COMPOUND)) {
                view.entity.fromTag(tag.getCompound("EntityData"));
            }

            return new MultipartPart((MultipartDefinition) definition, holder, view.state, view.entity);
        }
    }

    private static final class MultipartBufLoader implements IPartNetLoader {
        private final Multipart multipart;

        private MultipartBufLoader(Multipart multipart) {
            this.multipart = multipart;
        }

        @Override
        public AbstractPart loadFromBuffer(PartDefinition definition, MultipartHolder holder, NetByteBuf buffer, IMsgReadCtx ctx) {
            return new MultipartNbtReader(multipart).readFromNbt(definition, holder, buffer.readCompoundTag());
        }
    }
}

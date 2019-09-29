package juuxel.vanillaparts.api.part;

import com.google.common.collect.ImmutableMap;
import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.property.Property;

public class MultipartState extends AbstractPropertyContainer<Multipart, MultipartState> {
    public MultipartState(Multipart multipart, ImmutableMap<Property<?>, Comparable<?>> properties) {
        super(multipart, properties);
    }
}

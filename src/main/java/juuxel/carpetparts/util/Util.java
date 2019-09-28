package juuxel.carpetparts.util;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;

public class Util {
    public static <T> void visitRegistry(Registry<T> registry, BiConsumer<? super Identifier, ? super T> consumer) {
        for (Identifier id : registry.getIds()) {
            consumer.accept(id, registry.get(id));
        }

        RegistryEntryAddedCallback.event(registry).register(((rawId, id, object) -> consumer.accept(id, object)));
    }
}

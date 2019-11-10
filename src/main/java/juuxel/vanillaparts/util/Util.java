/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.util;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class Util {
    private Util() {}

    public static <T> void visitRegistry(Registry<T> registry, BiConsumer<? super Identifier, ? super T> consumer) {
        for (Identifier id : registry.getIds()) {
            consumer.accept(id, registry.get(id));
        }

        RegistryEntryAddedCallback.event(registry).register(((rawId, id, object) -> consumer.accept(id, object)));
    }

    public static <T> T safeGet(T[] ts, int index) {
        return ts[Math.abs(index % ts.length)];
    }

    public static <T> T on(T obj, Consumer<T> fn) {
        fn.accept(obj);
        return obj;
    }
}

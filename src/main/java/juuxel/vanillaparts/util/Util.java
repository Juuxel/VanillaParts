/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.util;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

    public static <T> T with(T obj, Consumer<T> fn) {
        fn.accept(obj);
        return obj;
    }

    /**
     * Gets the direction for which {@code origin.offset(direction).equals(target)}.
     *
     * @param origin the original position
     * @param target the target position
     * @return the target direction from the origin
     */
    public static Direction compare(BlockPos origin, BlockPos target) {
        int x = target.getX() - origin.getX();
        int y = target.getY() - origin.getY();
        int z = target.getZ() - origin.getZ();
        if (x == -1) return Direction.WEST;
        else if (x == 1) return Direction.EAST;
        else if (z == -1) return Direction.NORTH;
        else if (z == 1) return Direction.SOUTH;
        else if (y == -1) return Direction.DOWN;
        else return Direction.UP;
    }

    /**
     * Converts a multipart loot context to a block loot context builder.
     *
     * @param context the multipart loot context
     * @return the created builder
     */
    public static LootContext.Builder toBlockLootContext(LootContext context) {
        return new LootContext.Builder(context.getWorld())
                .random(context.getRandom())
                .luck(context.getLuck())
                .parameter(LootContextParameters.BLOCK_STATE, context.get(LootContextParameters.BLOCK_STATE))
                .parameter(LootContextParameters.ORIGIN, context.get(LootContextParameters.ORIGIN))
                .parameter(LootContextParameters.TOOL, context.get(LootContextParameters.TOOL))
                .optionalParameter(LootContextParameters.THIS_ENTITY, context.get(LootContextParameters.THIS_ENTITY))
                .optionalParameter(LootContextParameters.BLOCK_ENTITY, context.get(LootContextParameters.BLOCK_ENTITY))
                .optionalParameter(LootContextParameters.EXPLOSION_RADIUS, context.get(LootContextParameters.EXPLOSION_RADIUS));
    }
}

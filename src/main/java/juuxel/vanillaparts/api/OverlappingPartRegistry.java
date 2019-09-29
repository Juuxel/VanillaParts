package juuxel.vanillaparts.api;

import alexiil.mc.lib.multipart.api.PartDefinition;

import java.util.HashSet;
import java.util.Set;

public final class OverlappingPartRegistry {
    private static final Set<PartDefinition> OVERLAPPING_PARTS = new HashSet<>();

    public static boolean canOverlapWithCarpets(PartDefinition def) {
        return OVERLAPPING_PARTS.contains(def);
    }

    public static void register(PartDefinition def) {
        OVERLAPPING_PARTS.add(def);
    }
}

package juuxel.vanillaparts.part;

import juuxel.blockstoparts.api.category.Category;
import juuxel.vanillaparts.VanillaParts;

public final class VpCategories {
    public static final Category BUTTONS = register("buttons");
    public static final Category CAKE = register("cake");
    public static final Category CARPETS = register("carpets");
    public static final Category FENCES = register("fences");
    public static final Category LEVERS = register("levers");
    public static final Category SLABS = register("slabs");
    public static final Category TORCHES = register("torches");
    public static final Category REDSTONE_COMPONENTS = register("redstone_components");

    private static Category register(String name) {
        return Category.getOrRegister(VanillaParts.id(name));
    }
}

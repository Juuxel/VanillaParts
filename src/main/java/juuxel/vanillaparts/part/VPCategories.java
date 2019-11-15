package juuxel.vanillaparts.part;

import juuxel.blockstoparts.part.Categories;

public class VPCategories {
    public static final Categories BUTTON = of("button").build();
    public static final Categories CARPET = of("carpet").build();
    public static final Categories FENCE = of("fence").overlap("carpet").build();
    public static final Categories LEVER = of("lever").build();
    public static final Categories SIDING = of("siding").build();
    public static final Categories SLAB = of("slab").build();
    public static final Categories TORCH = of("torch").overlap("carpet").build();

    private static Categories.Builder of(String category) {
        return Categories.builder().add(category);
    }
}

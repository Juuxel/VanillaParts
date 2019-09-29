package juuxel.vanillaparts;

import juuxel.vanillaparts.api.part.Multipart;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

public final class Multiparts {
    public static final DefaultedRegistry<Multipart> REGISTRY = new DefaultedRegistry<>("vanilla_parts:red_carpet");

//    public static final Multipart WHITE_CARPET = register("white_carpet", new CarpetMultipart(Blocks.WHITE_CARPET, DyeColor.WHITE));
//    public static final Multipart ORANGE_CARPET = register("orange_carpet", new CarpetMultipart(Blocks.ORANGE_CARPET, DyeColor.ORANGE));
//    public static final Multipart MAGENTA_CARPET = register("magenta_carpet", new CarpetMultipart(Blocks.MAGENTA_CARPET, DyeColor.MAGENTA));
//    public static final Multipart LIGHT_BLUE_CARPET = register("light_blue_carpet", new CarpetMultipart(Blocks.LIGHT_BLUE_CARPET, DyeColor.LIGHT_BLUE));
//    public static final Multipart YELLOW_CARPET = register("yellow_carpet", new CarpetMultipart(Blocks.YELLOW_CARPET, DyeColor.YELLOW));
//    public static final Multipart LIME_CARPET = register("lime_carpet", new CarpetMultipart(Blocks.LIME_CARPET, DyeColor.LIME));
//    public static final Multipart PINK_CARPET = register("pink_carpet", new CarpetMultipart(Blocks.PINK_CARPET, DyeColor.PINK));
//    public static final Multipart GRAY_CARPET = register("gray_carpet", new CarpetMultipart(Blocks.GRAY_CARPET, DyeColor.GRAY));
//    public static final Multipart LIGHT_GRAY_CARPET = register("light_gray_carpet", new CarpetMultipart(Blocks.LIGHT_GRAY_CARPET, DyeColor.LIGHT_GRAY));
//    public static final Multipart CYAN_CARPET = register("cyan_carpet", new CarpetMultipart(Blocks.CYAN_CARPET, DyeColor.CYAN));
//    public static final Multipart PURPLE_CARPET = register("purple_carpket", new CarpetMultipart(Blocks.PURPLE_CARPET, DyeColor.PURPLE));
//    public static final Multipart BLUE_CARPET = register("blue_carpet", new CarpetMultipart(Blocks.BLUE_CARPET, DyeColor.BLUE));
//    public static final Multipart BROWN_CARPET = register("brown_carpet", new CarpetMultipart(Blocks.BROWN_CARPET, DyeColor.BROWN));
//    public static final Multipart GREEN_CARPET = register("green_carpet", new CarpetMultipart(Blocks.GREEN_CARPET, DyeColor.GREEN));
//    public static final Multipart RED_CARPET = register("red_carpet", new CarpetMultipart(Blocks.RED_CARPET, DyeColor.RED));
//    public static final Multipart BLACK_CARPET = register("black_carpet", new CarpetMultipart(Blocks.BLACK_CARPET, DyeColor.BLACK));

    static {
        Registry.register(Registry.REGISTRIES, VanillaParts.id("multiparts"), REGISTRY);
    }

    private Multiparts() {}

    private static Multipart register(String id, Multipart multipart) {
        return Registry.register(REGISTRY, VanillaParts.id(id), multipart);
    }
}

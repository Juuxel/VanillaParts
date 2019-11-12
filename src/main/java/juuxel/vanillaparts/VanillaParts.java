/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts;

import juuxel.vanillaparts.compat.Compat;
import juuxel.vanillaparts.item.TagStickItem;
import juuxel.vanillaparts.part.VPartDefinitions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class VanillaParts implements ModInitializer {
    public static Identifier id(String path) {
        return new Identifier("vanilla_parts", path);
    }

    @Override
    public void onInitialize() {
        VPartDefinitions.init();
        Compat.init();
        Registry.register(Registry.ITEM, id("tag_stick"), new TagStickItem(new Item.Settings()));

        // Register part placement tweak
        UseBlockCallback.EVENT.register(MultipartItemTweak.INSTANCE);
    }
}

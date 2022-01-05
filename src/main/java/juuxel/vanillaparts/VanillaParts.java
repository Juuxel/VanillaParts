/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts;

import juuxel.vanillaparts.lib.VpTags;
import juuxel.vanillaparts.part.PartConversions;
import juuxel.vanillaparts.part.VpParts;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.Identifier;

public final class VanillaParts implements ModInitializer {
    public static Identifier id(String path) {
        return new Identifier("vanilla_parts", path);
    }

    @Override
    public void onInitialize() {
        VpParts.init();
        VpTags.init();
        PartConversions.init();

        // Register part placement tweak
        UseBlockCallback.EVENT.register(MultipartItemTweak.INSTANCE);
    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts;

import juuxel.vanillaparts.compat.Compat;
import juuxel.vanillaparts.part.VPartDefinitions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.Identifier;

public final class VanillaParts implements ModInitializer {
    public static Identifier id(String path) {
        return new Identifier("vanilla_parts", path);
    }

    @Override
    public void onInitialize() {
        VPartDefinitions.init();
        Compat.init();

        // Register part placement tweak
        UseBlockCallback.EVENT.register(MultipartItemTweak.INSTANCE);
    }
}

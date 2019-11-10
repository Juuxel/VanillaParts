/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.compat;

import juuxel.vanillaparts.compat.extrapieces.ExtraPiecesCompat;
import net.fabricmc.loader.api.FabricLoader;

public final class Compat {
    public static void init() {
        ifModLoaded("extrapieces", ExtraPiecesCompat::init);
    }

    private static void ifModLoaded(String id, Runnable fn) {
        if (FabricLoader.getInstance().isModLoaded(id)) {
            fn.run();
        }
    }
}

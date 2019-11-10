/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface FenceExtensions {
    boolean vanillaParts_canConnect(IWorld world, BlockPos neighborPos);
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.part.model;

import juuxel.vanillaparts.part.VanillaPart;
import net.minecraft.block.BlockState;

public class DynamicVanillaModelKey extends VanillaModelKey {
    private final VanillaPart part;

    public DynamicVanillaModelKey(VanillaPart part) {
        this.part = part;
    }

    public BlockState getState() {
        return part.getBlockState();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return part.equals(((DynamicVanillaModelKey) o).part);
    }

    @Override
    public int hashCode() {
        return part.hashCode();
    }
}

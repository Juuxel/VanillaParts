/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.event;

import alexiil.mc.lib.multipart.api.event.MultipartEvent;
import net.minecraft.util.math.BlockPos;

// No @Environment(CLIENT) because this needs to be on the server

/**
 * A neighbour update event that is fired on the client.
 */
public class ClientNeighbourUpdateEvent extends MultipartEvent {
    public final BlockPos pos;

    public ClientNeighbourUpdateEvent(BlockPos pos) {
        this.pos = pos;
    }
}

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

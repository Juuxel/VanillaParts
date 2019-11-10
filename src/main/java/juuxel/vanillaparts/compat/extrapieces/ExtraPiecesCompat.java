/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */

package juuxel.vanillaparts.compat.extrapieces;

import alexiil.mc.lib.multipart.api.MultipartUtil;
import alexiil.mc.lib.multipart.api.PartDefinition;
import com.shnupbups.extrapieces.blocks.SidingPieceBlock;
import juuxel.vanillaparts.MultipartItemTweak;
import juuxel.vanillaparts.VanillaParts;
import juuxel.vanillaparts.util.Util;
import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public final class ExtraPiecesCompat {
    public static final Map<Block, PartDefinition> sidingParts = new HashMap<>();

    private static void register(PartDefinition def) {
        PartDefinition.PARTS.put(def.identifier, def);
    }

    public static void init() {
        MultipartItemTweak.INSTANCE.addExtension(SidingItemTweak.INSTANCE);
        MultipartItemTweak.INSTANCE.addCustomContainerChecker(SidingItemTweak.INSTANCE::isSiding);

        Util.visitRegistry(Registry.BLOCK, (id, block) -> {
            if (block instanceof SidingPieceBlock) {
                PartDefinition def = new PartDefinition(
                        VanillaParts.id("extrapieces/" + id.getNamespace() + "/" + id.getPath()),
                        (definition, holder, tag) -> new SidingPart(definition, holder, block, tag.getInt("Facing")),
                        (definition, holder, buf, ctx) -> new SidingPart(definition, holder, block, buf.readEnumConstant(Direction.class))
                );
                register(def);
                sidingParts.put(block, def);
            }
        });
    }
}

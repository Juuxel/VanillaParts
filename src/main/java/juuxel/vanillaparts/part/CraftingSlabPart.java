package juuxel.vanillaparts.part;

import alexiil.mc.lib.multipart.api.MultipartHolder;
import alexiil.mc.lib.multipart.api.PartDefinition;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class CraftingSlabPart extends SlabPart {
    public CraftingSlabPart(PartDefinition definition, MultipartHolder holder, SlabBlock block, BlockHalf half) {
        super(definition, holder, block, half);
    }

    public CraftingSlabPart(PartDefinition definition, MultipartHolder holder, SlabBlock block, CompoundTag tag) {
        super(definition, holder, block, tag);
    }

    public CraftingSlabPart(PartDefinition definition, MultipartHolder holder, SlabBlock block, boolean top) {
        super(definition, holder, block, top);
    }

    @Override
    public boolean onActivate(PlayerEntity player, Hand hand, BlockHitResult hit) {
        return getVanillaState().activate(player.world, player, hand, hit);
    }
}

package juuxel.vanillaparts.mixin;

import alexiil.mc.lib.multipart.api.MultipartUtil;
import juuxel.vanillaparts.block.VBlocks;
import juuxel.vanillaparts.part.CraftingSlabPart;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.CraftingTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(CraftingTableContainer.class)
public abstract class CraftingTableContainerMixin extends Container {
    @Shadow
    @Final
    private BlockContext context;

    private CraftingTableContainerMixin(ContainerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void onCanUse(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValueZ() && (canUse(this.context, player, VBlocks.CRAFTING_SLAB) || isCraftingSlabMultipart())) {
            info.setReturnValue(true);
        }
    }

    @Unique
    private boolean isCraftingSlabMultipart() {
        return context.run(MultipartUtil::get)
                .flatMap(Optional::ofNullable)
                .map(it -> !it.getParts(CraftingSlabPart.class).isEmpty())
                .orElse(false);
    }
}

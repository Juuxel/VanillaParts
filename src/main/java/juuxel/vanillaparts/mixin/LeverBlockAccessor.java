package juuxel.vanillaparts.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LeverBlock.class)
public interface LeverBlockAccessor {
    @SuppressWarnings("PublicStaticMixinMember")
    @Invoker
    static void callSpawnParticles(BlockState state, IWorld world, BlockPos pos, float f) {
        throw new AssertionError("LeverBlockAccessor called directly!");
    }
}

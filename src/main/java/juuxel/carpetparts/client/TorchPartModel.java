package juuxel.carpetparts.client;

import alexiil.mc.lib.multipart.api.render.PartModelBaker;
import alexiil.mc.lib.multipart.api.render.PartRenderContext;
import juuxel.carpetparts.part.TorchPart;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public enum TorchPartModel implements PartModelBaker<TorchPart.ModelKey> {
    INSTANCE;

    @Override
    public void emitQuads(TorchPart.ModelKey key, PartRenderContext ctx) {
        BlockState state;
        switch (key.getFacing()) {
            case NORTH:
                state = Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.NORTH);
                break;
            case EAST:
                state = Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.EAST);
                break;
            case SOUTH:
                state = Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.SOUTH);
                break;
            case WEST:
                state = Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.WEST);
                break;
            case GROUND:
            default:
                state = Blocks.TORCH.getDefaultState();
                break;
        }

        ctx.fallbackConsumer().accept(
                MinecraftClient.getInstance()
                        .getBakedModelManager()
                        .getBlockStateMaps()
                        .getModel(state)
        );
    }
}

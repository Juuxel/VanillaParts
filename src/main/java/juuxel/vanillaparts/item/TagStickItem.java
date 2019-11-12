package juuxel.vanillaparts.item;

import alexiil.mc.lib.multipart.api.AbstractPart;
import alexiil.mc.lib.multipart.api.MultipartContainer;
import alexiil.mc.lib.multipart.api.MultipartUtil;
import alexiil.mc.lib.multipart.impl.TransientPartIdentifier;
import alexiil.mc.lib.multipart.mixin.api.IBlockMultipart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TagStickItem extends Item {
    public TagStickItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IBlockMultipart<?>) {
            if (!world.isClient) {
                Object o = ((IBlockMultipart<?>) block).getTargetedMultipart(state, world, pos, context.getHitPos());
                if (o instanceof TransientPartIdentifier) {
                    AbstractPart part = ((TransientPartIdentifier) o).part;
                    PlayerEntity player = context.getPlayer();
                    if (player != null) {
                        player.sendMessage(new LiteralText(part.toTag().asString()));
                    }
                }
            }

            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
}

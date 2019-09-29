package juuxel.vanillaparts.api.part;

import alexiil.mc.lib.multipart.api.AbstractPart;
import alexiil.mc.lib.multipart.api.render.PartModelKey;
import juuxel.vanillaparts.part.model.StaticVanillaModelKey;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

import javax.annotation.Nullable;
import java.util.OptionalInt;

public abstract class Multipart {
    private final StateFactory<Multipart, MultipartState> stateFactory;
    private MultipartState defaultState;

    public Multipart() {
        StateFactory.Builder<Multipart, MultipartState> builder = new StateFactory.Builder<>(this);
        appendProperties(builder);
        this.stateFactory = builder.build(MultipartState::new);
        this.defaultState = stateFactory.getDefaultState();
    }

    //---------------------
    // States and entities
    //---------------------

    protected void appendProperties(StateFactory.Builder<Multipart, MultipartState> builder) {

    }

    public StateFactory<Multipart, MultipartState> getStateFactory() {
        return stateFactory;
    }

    public MultipartState getDefaultState() {
        return defaultState;
    }

    protected void setDefaultState(MultipartState state) {
        this.defaultState = state;
    }

    @Nullable
    public MultipartEntity createEntity(MultipartView.Mutable view) {
        return null;
    }

    //--------
    // Shapes
    //--------

    public abstract VoxelShape getOutlineShape(MultipartView view);

    public VoxelShape getCollisionShape(MultipartView view) {
        return getOutlineShape(view);
    }

    public VoxelShape getDynamicShape(MultipartView view, float partialTicks) {
        return getOutlineShape(view);
    }

    public boolean canOverlapWith(MultipartView view, MultipartView other) {
        return false;
    }

    // A variant of canOverlapWith for non-multipart parts
    public boolean canOverlapWith(MultipartView view, AbstractPart other) {
        return false;
    }

    //-----------------
    // Content methods
    //-----------------

    public boolean canEmitRedstone(MultipartView view) {
        return false;
    }

    public boolean use(MultipartView.Mutable view, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return false;
    }

    public OptionalInt getStrongRedstonePower(MultipartView view, Direction side) {
        return OptionalInt.empty();
    }

    public OptionalInt getWeakRedstonePower(MultipartView view, Direction side) {
        return OptionalInt.empty();
    }

    public MultipartState getStateForNeighborUpdate(MultipartView view, Direction side, BlockPos neighborPos) {
        return view.getMultipartState();
    }

    public ItemStack getPickStack(MultipartView view) {
        return ItemStack.EMPTY;
    }

    //------
    // Misc
    //------

    public PartModelKey getModelKey(MultipartView view) {
        BlockState blockState = toBlockState(view);
        if (blockState == null) throw new UnsupportedOperationException("Can't get block state model key from " + this);
        return new StaticVanillaModelKey(blockState);
    }

    @Nullable
    public BlockState toBlockState(MultipartView view) {
        return null;
    }

    @Override
    public String toString() {
        return "Multipart{" + this.getClass().getSimpleName() + "}";
    }
}

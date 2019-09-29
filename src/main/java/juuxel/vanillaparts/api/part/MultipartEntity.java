package juuxel.vanillaparts.api.part;

import net.minecraft.nbt.CompoundTag;

public class MultipartEntity {
    private final MultipartView.Mutable view;

    public MultipartEntity(MultipartView.Mutable view) {
        this.view = view;
    }

    public MultipartView.Mutable getView() {
        return view;
    }

    public void fromTag(CompoundTag tag) {}
    public CompoundTag toTag(CompoundTag tag) { return tag; }
}

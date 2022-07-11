package dustw.imi.blockentity;

import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import dustw.imi.blockentity.reg.ModBlockEntities;
import dustw.imi.menu.BaseChestMenu;
import lombok.Data;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author DustW
 **/
public class BaseChestBlockEntity extends ModBaseMenuBlockEntity<BaseChestBlockEntity.SaveObject, BaseChestBlockEntity.SyncObject> {
    public BaseChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.BASE_CHEST.get(), pWorldPosition, pBlockState);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, Player player) {
        return new BaseChestMenu(containerId, inventory, this);
    }

    @Data
    public static class SyncObject {
    }

    @Override
    protected SyncObject registerSyncObject() {
        return new SyncObject();
    }

    @Override
    protected Class<SyncObject> getSyncObjectClass() {
        return SyncObject.class;
    }

    @Data
    public static class SaveObject {
        private ItemStackHandler slots = new ItemStackHandler(36);
    }

    @Override
    protected SaveObject registerSaveObject() {
        return new SaveObject();
    }

    @Override
    protected Class<SaveObject> getSaveObjectClass() {
        return SaveObject.class;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> getSave().getSlots()));
    }
}

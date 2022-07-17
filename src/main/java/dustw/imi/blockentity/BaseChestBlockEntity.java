package dustw.imi.blockentity;

import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import dustw.imi.blockentity.reg.ModBlockEntities;
import dustw.imi.menu.BaseChestMenu;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tt432.millennium.sync.SyncDataManager;
import tt432.millennium.sync.object.ItemStackHandlerSyncData;

/**
 * @author DustW
 **/
public class BaseChestBlockEntity extends ModBaseMenuBlockEntity {
    public BaseChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.BASE_CHEST.get(), pWorldPosition, pBlockState);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, Player player) {
        return new BaseChestMenu(containerId, inventory, this);
    }

    @Getter
    private ItemStackHandlerSyncData slots;

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.slots.get()));
    }

    @Override
    protected void registerSyncData(SyncDataManager manager) {
        manager.add(slots = new ItemStackHandlerSyncData("slots", 36, true));
    }
}

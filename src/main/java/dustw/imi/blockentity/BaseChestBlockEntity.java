package dustw.imi.blockentity;

import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import dustw.imi.blockentity.reg.ModBlockEntities;
import dustw.imi.menu.BaseChestMenu;
import lombok.Data;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author DustW
 **/
public class BaseChestBlockEntity extends ModBaseMenuBlockEntity {
    public BaseChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.BASE_CHEST.get(), pWorldPosition, pBlockState);
    }

    @Getter
    private final ItemStackHandler slots = new ItemStackHandler(36);

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, Player player) {
        return new BaseChestMenu(containerId, inventory, this);
    }

    @Data
    public static class SyncObject {
    }

    @Override
    protected Object registerSyncObject() {
        return new SyncObject();
    }
}

package dustw.imi.menu;

import dustw.imi.blockentity.BaseChestBlockEntity;
import dustw.imi.menu.base.ModBaseBlockEntityMenu;
import dustw.imi.menu.reg.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author DustW
 **/
public class BaseChestMenu extends ModBaseBlockEntityMenu<BaseChestBlockEntity> {
    public BaseChestMenu(int windowId, Inventory inv, BaseChestBlockEntity blockEntity) {
        super(ModMenuTypes.BASE_CHEST.get(), windowId, inv, blockEntity);

        addSlotBox(blockEntity.getSlots(), 0, 10, 10, 9, 18, 4, 18);
    }
}

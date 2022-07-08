package dustw.imi.menu.base;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author DustW
 **/
public class ModBaseBlockEntityMenu<T extends BlockEntity> extends ModBaseMenu {
    public final T blockEntity;

    public ModBaseBlockEntityMenu(MenuType<?> type, int windowId, Inventory inv, T blockEntity) {
        super(type, windowId, inv);
        this.blockEntity = blockEntity;
    }
}

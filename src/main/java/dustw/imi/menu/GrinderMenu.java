package dustw.imi.menu;

import dustw.imi.blockentity.GrinderBlockEntity;
import dustw.imi.menu.base.ModBaseBlockEntityMenu;
import dustw.imi.menu.reg.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author DustW
 */
public class GrinderMenu extends ModBaseBlockEntityMenu<GrinderBlockEntity> {
    public GrinderMenu(int windowId, Inventory inv, GrinderBlockEntity blockEntity) {
        super(ModMenuTypes.GRINDER.get(), windowId, inv, blockEntity);

        addSlot(blockEntity.getInput().get(), 0, 1, 1);

        addResultSlot(blockEntity.getOutput().get(), 0, 1, 1);
    }
}

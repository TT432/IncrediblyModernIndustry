package dustw.imi.menu;

import dustw.imi.blockentity.ThermalBlockEntity;
import dustw.imi.menu.base.ModBaseBlockEntityMenu;
import dustw.imi.menu.reg.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author DustW
 **/
public class ThermalMenu extends ModBaseBlockEntityMenu<ThermalBlockEntity> {
    public ThermalMenu(int windowId, Inventory inv, ThermalBlockEntity blockEntity) {
        super(ModMenuTypes.THERMAL.get(), windowId, inv, blockEntity);

        addSlot(blockEntity.getBurnItem(), 0, 0, 0);
    }
}

package dustw.imi.menu;

import dustw.imi.blockentity.ElectricFurnaceBlocKEntity;
import dustw.imi.menu.base.ModBaseBlockEntityMenu;
import dustw.imi.menu.reg.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author DustW
 */
public class ElectricFurnaceMenu extends ModBaseBlockEntityMenu<ElectricFurnaceBlocKEntity> {
    public ElectricFurnaceMenu(int windowId, Inventory inv, ElectricFurnaceBlocKEntity blockEntity) {
        super(ModMenuTypes.ELECTRIC_FURNACE.get(), windowId, inv, blockEntity);

        addSlot(blockEntity.getInput().get(), 0, 1, 1);

        addResultSlot(blockEntity.getOutput().get(), 0, 1, 1);
    }
}

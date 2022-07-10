package dustw.imi.menu;

import dustw.imi.menu.base.ModBaseMenu;
import dustw.imi.menu.reg.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author DustW
 **/
public class TechTreeViewerMenu extends ModBaseMenu {
    public TechTreeViewerMenu(int pContainerId, Inventory inventory) {
        super(ModMenuTypes.TECH_TREE_VIEWER.get(), pContainerId, inventory);
    }
}

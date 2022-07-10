package dustw.imi.modernui.gui;

import dustw.imi.Imi;
import dustw.imi.menu.BaseChestMenu;
import dustw.imi.menu.reg.ModMenuTypes;
import icyllis.modernui.forge.OpenMenuEvent;
import icyllis.modernui.util.DataSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Imi.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OpenGuiHandler {
    @SubscribeEvent
    public static void openMenu(@Nonnull OpenMenuEvent event) {
        AbstractContainerMenu menu = event.getMenu();

        if (menu.getType() == ModMenuTypes.BASE_CHEST.get()) {
            BaseChestGui fragment = new BaseChestGui((BaseChestMenu) menu);
            fragment.setArguments(baseDataSet(menu));
            event.set(fragment);
        }

        if (menu.getType() == ModMenuTypes.TECH_TREE_VIEWER.get()) {
            TechTreeGui fragment = new TechTreeGui();
            fragment.setArguments(baseDataSet(menu));
            event.set(fragment);
        }
    }

    static DataSet baseDataSet(AbstractContainerMenu menu) {
        DataSet args = new DataSet();
        args.putInt("token", menu.containerId);
        return args;
    }
}

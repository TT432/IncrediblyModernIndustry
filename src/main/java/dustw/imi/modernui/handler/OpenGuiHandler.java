package dustw.imi.modernui.handler;

import dustw.imi.Imi;
import dustw.imi.menu.BaseChestMenu;
import dustw.imi.menu.ElectricFurnaceMenu;
import dustw.imi.menu.GrinderMenu;
import dustw.imi.menu.ThermalMenu;
import dustw.imi.menu.reg.ModMenuTypes;
import dustw.imi.modernui.gui.*;
import icyllis.modernui.forge.OpenMenuEvent;
import icyllis.modernui.fragment.Fragment;
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
            event.set(setBaseArguments(new BaseChestGui((BaseChestMenu) menu), menu));
        }
        else if (menu.getType() == ModMenuTypes.TECH_TREE_VIEWER.get()) {
            event.set(setBaseArguments(new TechTreeGui(), menu));
        }
        else if (menu.getType() == ModMenuTypes.THERMAL.get()) {
            event.set(setBaseArguments(new ThermalGui((ThermalMenu) menu), menu));
        }
        else if (menu.getType() == ModMenuTypes.GRINDER.get()) {
            event.set(setBaseArguments(new GrinderGui((GrinderMenu) menu), menu));
        }
        else if (menu.getType() == ModMenuTypes.ELECTRIC_FURNACE.get()) {
            event.set(setBaseArguments(new ElectricFurnaceGui((ElectricFurnaceMenu) menu), menu));
        }
    }

    static Fragment setBaseArguments(Fragment fragment, AbstractContainerMenu menu) {
        fragment.setArguments(baseDataSet(menu));
        return fragment;
    }

    static DataSet baseDataSet(AbstractContainerMenu menu) {
        DataSet args = new DataSet();
        args.putInt("token", menu.containerId);
        return args;
    }
}

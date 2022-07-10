package dustw.imi.menu.reg;

import dustw.imi.Imi;
import dustw.imi.menu.BaseChestMenu;
import dustw.imi.menu.TechTreeViewerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DustW
 **/
public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> REGISTER =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, Imi.MOD_ID);

    public static final RegistryObject<MenuType<BaseChestMenu>> BASE_CHEST =
            REGISTER.register("base_chest", () -> from(BaseChestMenu::new));

    public static final RegistryObject<MenuType<TechTreeViewerMenu>> TECH_TREE_VIEWER =
            REGISTER.register("tech_tree_viewer", () -> new MenuType<>(TechTreeViewerMenu::new));

    private interface ModBlockEntityContainerMenuCreator<M extends AbstractContainerMenu, T extends BlockEntity> {
        M create(int windowId, Inventory inv, T blockEntity);
    }

    private static <M extends AbstractContainerMenu, T extends BlockEntity> MenuType<M> from(ModBlockEntityContainerMenuCreator<M, T> creator) {
        return IForgeMenuType.create((id, inv, data) ->
                creator.create(id, inv, (T) inv.player.getLevel().getBlockEntity(data.readBlockPos())));
    }
}

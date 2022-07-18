package dustw.imi.datapack.reg;

import dustw.imi.Imi;
import dustw.imi.datapack.techtree.TechTreeManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * @author DustW
 */
@Mod.EventBusSubscriber
public class ModData {
    public static final DeferredRegister<TechTreeManager> REGISTER = DeferredRegister.create(TechTreeManager.REGISTRY_KEY, Imi.MOD_ID);
    public static final Supplier<IForgeRegistry<TechTreeManager>> REGISTRY = REGISTER.makeRegistry(TechTreeManager.class,
            () -> new RegistryBuilder<TechTreeManager>().disableSaving().dataPackRegistry(TechTreeManager.CODEC, TechTreeManager.CODEC));

    public static final RegistryObject<TechTreeManager> COMMON = REGISTER.register("tech_tree_manager", TechTreeManager::instance);

    @SubscribeEvent
    public static void onEvent(AddReloadListenerEvent event) {
        event.addListener(TechTreeManager.instance());
    }
}

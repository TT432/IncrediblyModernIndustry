package dustw.imi;

import dustw.imi.block.reg.ModBlocks;
import dustw.imi.blockentity.reg.ModBlockEntities;
import dustw.imi.item.reg.ModItems;
import dustw.imi.menu.reg.ModMenuTypes;
import dustw.imi.recipe.reg.ModRecipeTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author DustW
 */
@Mod(Imi.MOD_ID)
public class Imi {
    public static final String MOD_ID = "imi";

    public static final CreativeModeTab TAB = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BASE_CHEST.get());
        }
    };

    public Imi() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.REGISTER.register(bus);
        ModBlockEntities.REGISTER.register(bus);
        ModMenuTypes.REGISTER.register(bus);
        ModItems.REGISTER.register(bus);
        ModRecipeTypes.REGISTER.register(bus);
    }
}

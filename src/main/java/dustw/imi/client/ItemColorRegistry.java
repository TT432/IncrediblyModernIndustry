package dustw.imi.client;

import dustw.imi.item.reg.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author DustW
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemColorRegistry {
    @SubscribeEvent
    public static void onEvent(FMLClientSetupEvent event) {
        ItemColors itemColors = Minecraft.getInstance().getItemColors();

        itemColors.register((s, i) -> 0xFFd8d8d8, ModItems.IRON_POWDER.get());
        itemColors.register((s, i) -> 0xFFfdf55f, ModItems.GOLD_POWDER.get());
        itemColors.register((s, i) -> 0xFF8f8f8f, ModItems.STONE_POWDER.get());
        itemColors.register((s, i) -> 0xFFaf8f55, ModItems.WOOD_POWDER.get());
        itemColors.register((s, i) -> 0xFF2e2e2e, ModItems.COAL_POWDER.get());
    }
}

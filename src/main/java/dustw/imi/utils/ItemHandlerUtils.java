package dustw.imi.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DustW
 */
public class ItemHandlerUtils {
    public static List<ItemStack> from(IItemHandler... handlers) {
        List<ItemStack> result = new ArrayList<>();

        for (IItemHandler handler : handlers) {
            for (int i = 0; i < handler.getSlots(); i++) {
                result.add(handler.getStackInSlot(i));
            }
        }

        return result;
    }
}

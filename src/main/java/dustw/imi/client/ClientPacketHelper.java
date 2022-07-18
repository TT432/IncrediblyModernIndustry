package dustw.imi.client;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author DustW
 **/
public class ClientPacketHelper {
    public static void handleInventoryMouseClick(int pContainerId, int pSlotId, int pMouseButton, ClickType pClickType) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) {
            return;
        }

        AbstractContainerMenu abstractcontainermenu = player.containerMenu;
        NonNullList<Slot> nonnulllist = abstractcontainermenu.slots;
        int i = nonnulllist.size();
        List<ItemStack> list = Lists.newArrayListWithCapacity(i);

        for(Slot slot : nonnulllist) {
            list.add(slot.getItem().copy());
        }

        abstractcontainermenu.clicked(pSlotId, pMouseButton, pClickType, player);
        Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();

        for(int j = 0; j < i; ++j) {
            ItemStack itemstack = list.get(j);
            ItemStack itemstack1 = nonnulllist.get(j).getItem();
            if (!ItemStack.matches(itemstack, itemstack1)) {
                int2objectmap.put(j, itemstack1.copy());
            }
        }

        player.connection.send(new ServerboundContainerClickPacket(pContainerId, abstractcontainermenu.getStateId(), pSlotId, pMouseButton, pClickType, abstractcontainermenu.getCarried().copy(), int2objectmap));
    }
}

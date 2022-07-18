package dustw.imi.modernui.gui.base;

import dustw.imi.blockentity.base.ModBaseMenuBlockEntity;
import dustw.imi.client.PacketHelper;
import dustw.imi.menu.base.ModBaseBlockEntityMenu;
import dustw.imi.menu.base.ModBaseMenu;
import dustw.imi.modernui.component.button.ItemSlotButton;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.lwjgl.glfw.GLFW;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author DustW
 **/
public class ModFragment extends Fragment {
    protected LinearLayout.LayoutParams centerH() {
        var result = warp();
        result.gravity = Gravity.CENTER_HORIZONTAL;
        return result;
    }

    protected LinearLayout.LayoutParams centerH(int size) {
        var result = new LinearLayout.LayoutParams(size, size);
        result.gravity = Gravity.CENTER_HORIZONTAL;
        return result;
    }

    protected LinearLayout.LayoutParams centerH(int left, int top, int right, int bottom) {
        var result = centerH();
        result.setMargins(dp(left), dp(top), dp(right), dp(bottom));
        return result;
    }

    protected LinearLayout.LayoutParams center() {
        return center(WRAP_CONTENT);
    }

    protected LinearLayout.LayoutParams center(int size) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    protected LinearLayout.LayoutParams center(int left, int top, int right, int bottom) {
        var result = center();
        result.setMargins(dp(left), dp(top), dp(right), dp(bottom));
        return result;
    }

    protected LinearLayout.LayoutParams center(int size, int left, int top, int right, int bottom) {
        var result = center(size);
        result.setMargins(dp(left), dp(top), dp(right), dp(bottom));
        return result;
    }

    protected LinearLayout.LayoutParams left(int left, int top, int right, int bottom) {
        var result = left();
        result.setMargins(dp(left), dp(top), dp(right), dp(bottom));
        return result;
    }

    protected LinearLayout.LayoutParams left() {
        var result = warp();
        result.gravity = Gravity.LEFT;
        return result;
    }

    protected LinearLayout.LayoutParams warp(int left, int top, int right, int bottom) {
        var result = warp();
        result.setMargins(dp(left), dp(top), dp(right), dp(bottom));
        return result;
    }

    protected LinearLayout.LayoutParams warp() {
        return new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
    }

    protected LinearLayout playerInv(ModBaseMenu menu) {
        var playerInventory = new LinearLayout();
        playerInventory.setOrientation(LinearLayout.VERTICAL);

        var playerInventoryLine1 = new LinearLayout();
        var playerInventoryLine2 = new LinearLayout();
        var playerInventoryLine3 = new LinearLayout();
        var playerInventoryLine4 = new LinearLayout();

        LinearLayout[] lines = {
                playerInventoryLine1,
                playerInventoryLine2,
                playerInventoryLine3,
                playerInventoryLine4
        };

        for (int i = 0; i < 36; i++) {
            var index = i / 9;

            var button = button(menu, i, false);

            int size = dp(38);
            var params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(3, 3, 3, 3);

            lines[index].addView(button, params);
        }

        playerInventory.addView(playerInventoryLine1, centerH());
        playerInventory.addView(playerInventoryLine2, centerH());
        playerInventory.addView(playerInventoryLine3, centerH());
        playerInventory.addView(playerInventoryLine4, centerH(0, 10, 0, 0));

        return playerInventory;
    }

    protected ItemSlotButton button(AbstractContainerMenu menu, int index, boolean isArc) {
        ItemSlotButton button = new ItemSlotButton(menu.getSlot(index));

        button.getBackgroundDrawable().setArc(isArc);

        button.setOnClickListener(v -> {
            // TODO 快速移动物品（Shift + 左）
            PacketHelper.handleInventoryMouseClick(menu.containerId, index, GLFW.GLFW_MOUSE_BUTTON_LEFT, ClickType.PICKUP);
            button.invalidate();
        });

        return button;
    }

    protected TextView invName(Inventory inventory) {
        var invName = new TextView();
        invName.setText(inventory.getDisplayName().getString());
        invName.setTextSize(16);
        invName.setTextColor(0xFF808080);
        return invName;
    }

    protected TextView menuName(ModBaseBlockEntityMenu<? extends ModBaseMenuBlockEntity> menu) {
        var menuName = new TextView();
        menuName.setText(menu.blockEntity.getDisplayName().getString());
        menuName.setTextSize(16);
        menuName.setTextColor(0xFF808080);
        return menuName;
    }
}

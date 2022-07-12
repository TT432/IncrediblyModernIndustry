package dustw.imi.modernui.gui;

import dustw.imi.client.PacketHelper;
import dustw.imi.menu.BaseChestMenu;
import dustw.imi.modernui.UiUtils;
import dustw.imi.modernui.component.button.SlotButton;
import dustw.imi.modernui.component.drawable.BackgroundDrawable;
import dustw.imi.modernui.gui.base.ModFragment;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;
import lombok.AllArgsConstructor;
import net.minecraft.world.inventory.ClickType;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author DustW
 **/
@AllArgsConstructor
public class BaseChestGui extends ModFragment {
    BaseChestMenu menu;

    @Nullable
    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        var content = new LinearLayout();

        var in = new LinearLayout();
        in.setOrientation(LinearLayout.VERTICAL);

        var title = new TextView();
        title.setText(menu.blockEntity.getDisplayName().getString());
        title.setTextSize(16);
        title.setTextColor(0xFF808080);
        in.addView(title, left(15, 10, 10, 5));

        var chestSlots = new LinearLayout();
        chestSlots.setOrientation(LinearLayout.VERTICAL);

        var playerInventory = new LinearLayout();
        playerInventory.setOrientation(LinearLayout.VERTICAL);

        var playerInventoryLine1 = new LinearLayout();
        var playerInventoryLine2 = new LinearLayout();
        var playerInventoryLine3 = new LinearLayout();
        var playerInventoryLine4 = new LinearLayout();

        var chestSlotsLine1 = new LinearLayout();
        var chestSlotsLine2 = new LinearLayout();
        var chestSlotsLine3 = new LinearLayout();
        var chestSlotsLine4 = new LinearLayout();

        var lines = new LinearLayout[] {
                playerInventoryLine1,
                playerInventoryLine2,
                playerInventoryLine3,
                playerInventoryLine4,

                chestSlotsLine1,
                chestSlotsLine2,
                chestSlotsLine3,
                chestSlotsLine4
        };

        int slotAmount = menu.slots.size();

        for (int i = 0; i < slotAmount; i++) {
            var index = i / 9;

            if (index < lines.length) {
                var button = new SlotButton(menu.slots.get(i));

                int size = dp(38);
                var params = new LinearLayout.LayoutParams(size, size);
                params.setMargins(3, 3, 3, 3);

                int finalI = i;
                button.setOnClickListener(v -> {
                    // TODO 快速移动物品（Shift + 左）
                    PacketHelper.handleInventoryMouseClick(menu.containerId, finalI, GLFW.GLFW_MOUSE_BUTTON_LEFT, ClickType.PICKUP);
                    button.invalidate();
                });

                lines[index].addView(button, params);
            }
        }

        playerInventory.addView(playerInventoryLine1, centerH());
        playerInventory.addView(playerInventoryLine2, centerH());
        playerInventory.addView(playerInventoryLine3, centerH());
        playerInventory.addView(playerInventoryLine4, centerH(0, 10, 0, 0));

        chestSlots.addView(chestSlotsLine1, centerH());
        chestSlots.addView(chestSlotsLine2, centerH());
        chestSlots.addView(chestSlotsLine3, centerH());
        chestSlots.addView(chestSlotsLine4, centerH());

        var slots = new LinearLayout();
        slots.setOrientation(LinearLayout.VERTICAL);

        slots.addView(chestSlots, center());

        var invName = UiUtils.invName(menu.inventory);
        slots.addView(invName, left(5, 5, 5, 5));

        slots.addView(playerInventory, center());

        in.addView(slots, center(10, 0, 10, 10));

        BackgroundDrawable background = new BackgroundDrawable();
        in.setBackground(background);

        content.addView(in, center());

        content.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));

        return content;
    }
}

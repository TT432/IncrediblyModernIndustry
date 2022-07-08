package dustw.imi.modernui.gui;

import dustw.imi.menu.BaseChestMenu;
import dustw.imi.modernui.button.SlotButton;
import dustw.imi.modernui.drawable.BackgroundDrawable;
import dustw.imi.modernui.drawable.SlotDrawable;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import org.jetbrains.annotations.Nullable;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author DustW
 **/
public class BaseChestGui extends Fragment {
    BaseChestMenu menu;

    public BaseChestGui(BaseChestMenu menu) {
        this.menu = menu;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        var content = new LinearLayout();

        var in = new LinearLayout();
        in.setOrientation(LinearLayout.VERTICAL);

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

        for (int i = 0; i < menu.slots.size(); i++) {
            var index = i / 9;

            if (index < lines.length) {
                var button = new SlotButton(menu.slots.get(i));
                SlotDrawable slot = new SlotDrawable(menu.slots.get(i));

                int size = dp(36);
                var params = new LinearLayout.LayoutParams(size, size);
                params.setMargins(2, 2, 2, 2);

                button.setBackground(slot);
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
        slots.addView(playerInventory, center(0, 25, 0, 0));

        in.addView(slots, center(10, 25, 10, 10));

        BackgroundDrawable background = new BackgroundDrawable();
        in.setBackground(background);

        content.addView(in, center());

        content.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));

        return content;
    }

    LinearLayout.LayoutParams centerH() {
        return new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
    }

    LinearLayout.LayoutParams centerH(int left, int top, int right, int bottom) {
        var result = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
        result.setMargins(dp(left), dp(top), dp(right), dp(bottom));
        return result;
    }

    LinearLayout.LayoutParams center() {
        return new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER);
    }

    LinearLayout.LayoutParams center(int left, int top, int right, int bottom) {
        var result = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER);
        result.setMargins(dp(left), dp(top), dp(right), dp(bottom));
        return result;
    }
}
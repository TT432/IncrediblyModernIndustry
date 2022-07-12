package dustw.imi.modernui.gui;

import dustw.imi.blockentity.component.ChangeListenerEnergyStorage;
import dustw.imi.client.PacketHelper;
import dustw.imi.menu.ThermalMenu;
import dustw.imi.modernui.UiUtils;
import dustw.imi.modernui.component.button.SlotButton;
import dustw.imi.modernui.component.drawable.BackgroundDrawable;
import dustw.imi.modernui.component.drawable.CircularProgressDrawable;
import dustw.imi.modernui.gui.base.ModFragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.lifecycle.MutableLiveData;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;
import lombok.AllArgsConstructor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author DustW
 **/
@AllArgsConstructor
public class ThermalGui extends ModFragment {
    ThermalMenu menu;

    final MutableLiveData<Integer> energyData = new MutableLiveData<>();
    final MutableLiveData<Integer> burnData = new MutableLiveData<>();

    @Nullable
    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        LinearLayout content = new LinearLayout();
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(10), dp(10), dp(10), dp(10));

        var title = new TextView();
        title.setText(menu.blockEntity.getDisplayName().getString());
        title.setTextSize(16);
        title.setTextColor(0xFF808080);
        content.addView(title, left(5, 0, 0, 5));

        LinearLayout progressLayout = new LinearLayout();
        progressLayout.setOrientation(LinearLayout.VERTICAL);
        progressLayout.setGravity(Gravity.CENTER);

        FrameLayout energyView = new FrameLayout();
        ChangeListenerEnergyStorage energyStorage = menu.blockEntity.getEnergyStorage();
        energyStorage.setChangeListener(storage -> energyData.postValue(storage.getEnergyStored()));
        var energy = new CircularProgressDrawable(energyData, energyStorage.getMaxEnergyStored());
        energy.setStartAngle(-90);
        energyView.setBackground(energy);

        FrameLayout burnView = new FrameLayout();
        var burn = new CircularProgressDrawable(burnData, menu.blockEntity.getSync().getMaxBurnTick(), 0xFFC0392B);
        burn.setStartAngle(-90);
        menu.blockEntity.setBurnTickChangeListener(be -> {
            burnData.postValue(be.getSync().getBurnTick());
            burn.setMax(be.getSync().getMaxBurnTick());
        });
        burnView.setBackground(burn);

        SlotButton button = button(menu, 36, true);
        int size = dp(36);
        burnView.addView(button, new FrameLayout.LayoutParams(size, size, Gravity.CENTER));

        energyView.addView(burnView, new FrameLayout.LayoutParams(dp(80), dp(80), Gravity.CENTER));

        burnData.observe(this, integer -> {
            burnView.invalidate();
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp(160), dp(160));
        params.setMargins(dp(5), dp(5), dp(5), dp(5));
        progressLayout.addView(energyView, params);

        TextView textView = new TextView() {
            @Override
            protected void onDraw(@NotNull Canvas canvas) {
                super.onDraw(canvas);
                invalidate();
            }
        };

        energyData.observe(this, integer -> {
            energyView.invalidate();
            textView.setText(energyStorage.getEnergyStored() + " / " + energyStorage.getMaxEnergyStored());
        });

        title.setTextSize(16);
        title.setTextColor(0xFF808080);
        progressLayout.addView(textView, centerH());

        content.addView(progressLayout, center());

        var invName = UiUtils.invName(menu.inventory);
        content.addView(invName, left(5, 5, 5, 5));

        content.addView(playerInv(), center());

        content.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
        content.setBackground(new BackgroundDrawable());
        return content;
    }

    LinearLayout playerInv() {
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

        playerInventory.addView(playerInventoryLine1, centerH());
        playerInventory.addView(playerInventoryLine2, centerH());
        playerInventory.addView(playerInventoryLine3, centerH());
        playerInventory.addView(playerInventoryLine4, centerH(0, 10, 0, 0));

        return playerInventory;
    }

    SlotButton button(AbstractContainerMenu menu, int index, boolean isArc) {
        SlotButton button = new SlotButton(menu.getSlot(index));

        button.getBackgroundDrawable().setArc(isArc);

        button.setOnClickListener(v -> {
            // TODO 快速移动物品（Shift + 左）
            PacketHelper.handleInventoryMouseClick(menu.containerId, index, GLFW.GLFW_MOUSE_BUTTON_LEFT, ClickType.PICKUP);
            button.invalidate();
        });

        return button;
    }
}

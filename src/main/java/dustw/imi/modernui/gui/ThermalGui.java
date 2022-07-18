package dustw.imi.modernui.gui;

import dustw.imi.blockentity.component.ChangeListenerEnergyStorage;
import dustw.imi.menu.ThermalMenu;
import dustw.imi.modernui.component.button.ItemSlotButton;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        content.addView(menuName(menu), left(5, 0, 0, 5));

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
        var burn = new CircularProgressDrawable(burnData, menu.blockEntity.getMaxBurnTick().get(), 0xFFC0392B);
        burn.setStartAngle(-90);
        menu.blockEntity.setBurnTickChangeListener(be -> {
            burnData.postValue(be.getBurnTick().get());
            burn.setMax(be.getMaxBurnTick().get());
        });
        burnView.setBackground(burn);

        ItemSlotButton button = button(menu, 36, true);
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

        textView.setTextSize(16);
        textView.setTextColor(0xFF808080);
        progressLayout.addView(textView, centerH());

        content.addView(progressLayout, center());

        content.addView(invName(menu.inventory), left(5, 5, 5, 5));

        content.addView(playerInv(menu), center());

        content.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
        content.setBackground(new BackgroundDrawable());
        return content;
    }
}

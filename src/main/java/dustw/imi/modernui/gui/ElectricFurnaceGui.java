package dustw.imi.modernui.gui;

import dustw.imi.blockentity.GrinderBlockEntity;
import dustw.imi.blockentity.component.ChangeListenerEnergyStorage;
import dustw.imi.menu.ElectricFurnaceMenu;
import dustw.imi.modernui.component.drawable.BackgroundDrawable;
import dustw.imi.modernui.component.drawable.CircularProgressDrawable;
import dustw.imi.modernui.component.drawable.ProgressDrawable;
import dustw.imi.modernui.gui.base.ModFragment;
import icyllis.modernui.core.Core;
import icyllis.modernui.lifecycle.MutableLiveData;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author DustW
 */
@AllArgsConstructor
public class ElectricFurnaceGui extends ModFragment {
    ElectricFurnaceMenu menu;

    final MutableLiveData<Integer> energyData = new MutableLiveData<>();
    final MutableLiveData<Integer> progressData = new MutableLiveData<>();

    @Nullable
    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        LinearLayout content = new LinearLayout();
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(10), dp(10), dp(10), dp(10));

        content.addView(menuName(menu), left(5, 0, 0, 5));

        LinearLayout up = new LinearLayout();
        up.setGravity(Gravity.CENTER);

        FrameLayout left = new FrameLayout();
        int leftSize = dp(160);

        int size = dp(38);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);

        left.addView(button(menu, 36, true), new FrameLayout.LayoutParams(size, size, Gravity.CENTER));

        ChangeListenerEnergyStorage energy = menu.blockEntity.getEnergy();
        energy.setChangeListener(s -> energyData.postValue(energy.getEnergyStored()));
        CircularProgressDrawable background = new CircularProgressDrawable(energyData, GrinderBlockEntity.MAX_ENERGY);
        background.setStartAngle(-90);
        left.setBackground(background);

        up.addView(left, new LinearLayout.LayoutParams(leftSize, leftSize));

        FrameLayout progress = new FrameLayout();
        ProgressDrawable background1 = new ProgressDrawable(progressData, menu.blockEntity.getMaxCraftTick().get());
        menu.blockEntity.setCraftTickChangedListener(() -> {
            int currentProgress = menu.blockEntity.getMaxCraftTick().get() - menu.blockEntity.getCraftTick().get();
            background1.setMax(menu.blockEntity.getMaxCraftTick().get());
            progressData.postValue(currentProgress);
            Core.getUiHandlerAsync().post(progress::invalidate);
        });
        progress.setBackground(background1);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(dp(100), dp(30));
        params1.setMargins(dp(10), dp(10), dp(10), dp(10));
        up.addView(progress, params1);

        up.addView(button(menu, 37, false), params);

        content.addView(up, center());

        content.addView(invName(menu.inventory), left(5, 5, 5, 5));

        content.addView(playerInv(menu), center());

        content.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
        content.setBackground(new BackgroundDrawable());
        return content;
    }
}
package dustw.imi.modernui.gui;

import com.mojang.blaze3d.platform.Window;
import dustw.imi.modernui.button.TechEntryButton;
import dustw.imi.modernui.drawable.BackgroundDrawable;
import dustw.imi.techtree.TechEntry;
import dustw.imi.techtree.TechTreeManager;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.*;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author DustW
 **/
public class TechTreeGui extends ModFragment {
    TechEntryButton selectButton;
    TechEntry select;
    TextView name;
    TextView description;

    @Nullable
    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        LinearLayout content = new LinearLayout();
        Window window = Minecraft.getInstance().getWindow();
        int width = window.getWidth();
        int height = window.getHeight();

        {
            LinearLayout left = new LinearLayout();
            left.setOrientation(LinearLayout.VERTICAL);

            {
                LinearLayout infoPlate = new LinearLayout();
                infoPlate.setOrientation(LinearLayout.VERTICAL);

                {
                    name = new TextView();
                    name.setTextSize(24);
                    name.setTextColor(0xFF808080);
                    infoPlate.addView(name, left(15, 15, 0, 0));
                }

                {
                    description = new TextView();
                    description.setTextSize(14);
                    description.setTextColor(0xFF808080);
                    infoPlate.addView(description, left(15, 10, 0, 0));
                    infoPlate.setBackground(new BackgroundDrawable());
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, (int) (height * 0.3));
                params.setMargins(0, 0, 0, dp(5));
                left.addView(infoPlate, params);
            }

            {
                // TODO 正在做的研究
            }

            {
                // TODO 推荐做的研究
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width * 0.3), height - dp(20));
            params.setMargins(dp(10), dp(10), dp(7), dp(10));
            content.addView(left, params);
        }

        {
            // TODO 不能滚动
            ScrollView right = new ScrollView();

            LinearLayout skillTree = new LinearLayout();
            skillTree.setClipChildren(false);
            skillTree.setPadding(dp(10), dp(10), dp(10), dp(10));

            List<TechEntry> techEntries = TechTreeManager.rootEntries();

            addTechEntryButton(skillTree, techEntries, null);

            right.addView(skillTree, new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

            right.setBackground(new BackgroundDrawable());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width * 0.7 - dp(10 + 10 + 7)), height - dp(20));
            params.setMargins(0, dp(10), dp(10), dp(10));
            content.addView(right, params);
        }

        content.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return content;
    }

    void setSelect(TechEntry value, TechEntryButton button) {
        if (selectButton != null) {
            selectButton.setSelect(false);
            selectButton.invalidate();
        }

        selectButton = button;

        button.setSelect(true);
        button.invalidate();

        select = value;

        name.setText(value.getNameComponent().getString());
        name.invalidate();

        description.setText(value.getDescriptionComponent().getString());
        name.invalidate();
    }

    void addTechEntryButton(LinearLayout parent, List<TechEntry> entries, TechEntryButton parentButton) {
        for (TechEntry value : entries) {
            LinearLayout valueLayout = new LinearLayout();
            valueLayout.setOrientation(LinearLayout.VERTICAL);
            valueLayout.setClipChildren(false);

            TechEntryButton next;

            {
                LinearLayout top = new LinearLayout();
                top.setGravity(Gravity.CENTER);
                top.setClipChildren(false);

                TechEntryButton techEntryButton = new TechEntryButton();

                techEntryButton.setOnClickListener(view -> setSelect(value, techEntryButton));

                if (value.getIconPath() != null) {
                    ImageDrawable drawable = new ImageDrawable(value.getIconPath().getNamespace(), value.getIconPath().getPath());
                    techEntryButton.setImageDrawable(drawable);
                    techEntryButton.setPadding(dp(5), dp(5), dp(5), dp(5));
                }

                BackgroundDrawable background = new BackgroundDrawable();
                techEntryButton.setBounding(background);
                techEntryButton.setBackground(background);

                if (parentButton != null) {
                    techEntryButton.setParent(parentButton);
                }

                next = techEntryButton;

                int size = dp(50);
                top.addView(techEntryButton, center(size, 5, 5, 5, 5));

                valueLayout.addView(top, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            }

            {
                if (value.getChildren() != null) {
                    LinearLayout below = new LinearLayout();
                    below.setClipChildren(false);

                    addTechEntryButton(below, value.getChildren(), next);

                    valueLayout.addView(below, warp());
                }
            }

            parent.addView(valueLayout, warp());
        }
    }
}

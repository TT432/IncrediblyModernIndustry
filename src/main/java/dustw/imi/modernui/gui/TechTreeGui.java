package dustw.imi.modernui.gui;

import com.mojang.blaze3d.platform.Window;
import dustw.imi.datapack.techtree.TechEntry;
import dustw.imi.datapack.techtree.TechTreeManager;
import dustw.imi.modernui.component.button.TechEntryButton;
import dustw.imi.modernui.component.drawable.BackgroundDrawable;
import dustw.imi.modernui.component.view.HorizontalScrollView;
import dustw.imi.modernui.component.view.ItemView;
import dustw.imi.modernui.gui.base.ModFragment;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.TextView;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

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

    /** 旧 - 新 */
    List<BiConsumer<TechEntry, TechEntry>> selectChanged = new ArrayList<>();

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
                LinearLayout makePlane = new LinearLayout();
                makePlane.setOrientation(LinearLayout.VERTICAL);
                makePlane.setPadding(dp(5), dp(5), dp(5), dp(5));

                {
                    HorizontalScrollView needItems = new HorizontalScrollView();
                    needItems.setPadding(dp(5), dp(5), dp(5), dp(5));
                    LinearLayout inner = new LinearLayout();

                    int size = dp(38);

                    selectChanged.add((old, newer) -> {
                        if (newer != null) {
                            inner.removeAllViews();

                            newer.getRequired().forEach((ingredient, integer) -> {
                                ItemStack item = ingredient.getItems()[0].copy();
                                item.setCount(integer);
                                ItemView child = new ItemView(item);
                                child.setBackground(new BackgroundDrawable());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                                params.setMargins(0, 0, dp(5), 0);
                                inner.addView(child, params);
                            });
                        }
                    });

                    needItems.addView(inner, new FrameLayout.LayoutParams(size, WRAP_CONTENT, Gravity.LEFT));
                    makePlane.addView(needItems, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                }

                {
                    // TODO 确认按钮
                }

                makePlane.setBackground(new BackgroundDrawable());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                params.setMargins(0, 0, 0, dp(5));
                left.addView(makePlane, params);
            }

            {
                // TODO 推荐做的研究
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width * 0.3), height - dp(20));
            params.setMargins(dp(10), dp(10), dp(7), dp(10));
            content.addView(left, params);
        }

        {
            ScrollView right = new ScrollView() {
                @Override
                public boolean onTouchEvent(@NotNull MotionEvent ev) {
                    return super.onTouchEvent(ev) | getChildAt(0).onTouchEvent(ev);
                }

                @Override
                public boolean onInterceptTouchEvent(@NotNull MotionEvent ev) {
                    return super.onInterceptTouchEvent(ev) | ((HorizontalScrollView) getChildAt(0)).onInterceptTouchEvent(ev);
                }
            };
            right.setVerticalScrollBarEnabled(false);
            right.setPadding(dp(10), dp(10), dp(10), dp(10));

            HorizontalScrollView in = new HorizontalScrollView();
            in.setHorizontalScrollBarEnabled(false);

            LinearLayout skillTree = new LinearLayout();
            skillTree.setClipChildren(false);

            List<TechEntry> techEntries = TechTreeManager.rootEntries();

            addTechEntryButton(skillTree, techEntries, null);

            in.addView(skillTree, new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

            right.addView(in, new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
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

        selectChanged.forEach(a -> a.accept(select, value));

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

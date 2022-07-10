package dustw.imi.modernui.gui;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.widget.LinearLayout;

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
}

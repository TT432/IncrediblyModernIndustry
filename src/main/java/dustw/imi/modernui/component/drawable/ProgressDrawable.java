package dustw.imi.modernui.component.drawable;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.lifecycle.LiveData;
import icyllis.modernui.math.Rect;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import static icyllis.modernui.view.View.dp;

/**
 * @author DustW
 */
@Setter
public class ProgressDrawable extends Drawable {
    final LiveData<Integer> holder;
    int max;
    int color;
    int backGroundColor = 0xFF2C3E50;
    int radius = dp(8);
    BackgroundDrawable backgroundDrawable;

    public ProgressDrawable(LiveData<Integer> holder, int max) {
        this(holder, max, 0xFF3498DB);
    }

    public ProgressDrawable(LiveData<Integer> holder, int max, int color) {
        this.holder = holder;
        this.max = max;
        this.color = color;

        backgroundDrawable = new BackgroundDrawable();
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        Rect b = getBounds();

        if (holder.getValue() != null && max != 0) {
            float x = holder.getValue() * 1f / max * b.right;

            backgroundDrawable.setColor(backGroundColor);
            backgroundDrawable.setBounds(b);

            backgroundDrawable.draw(canvas);

            Paint take = Paint.take();
            take.setColor(color);
            canvas.drawRoundRect(0, 0, x, b.bottom, radius, take);
        }
    }
}

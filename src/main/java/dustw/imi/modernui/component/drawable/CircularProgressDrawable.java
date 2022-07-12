package dustw.imi.modernui.component.drawable;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.lifecycle.LiveData;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import static icyllis.modernui.view.View.dp;

/**
 * @author DustW
 **/
@Setter
public class CircularProgressDrawable extends Drawable {
    final LiveData<Integer> holder;
    int max;
    int color;
    int backGroundColor = 0xFF2C3E50;
    float startAngle;
    BackgroundDrawable backgroundDrawable;

    public CircularProgressDrawable(LiveData<Integer> holder, int max) {
        this(holder, max, 0xFF3498DB);
    }

    public CircularProgressDrawable(LiveData<Integer> holder, int max, int color) {
        this.holder = holder;
        this.max = max;
        this.color = color;

        backgroundDrawable = new BackgroundDrawable();
        backgroundDrawable.setArc(true);
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        Paint paint = Paint.take();
        paint.setColor(color);

        if (holder.getValue() != null) {
            var b = getBounds();

            backgroundDrawable.setColor(backGroundColor);
            backgroundDrawable.setBounds(b);

            backgroundDrawable.draw(canvas);

            float cx = b.centerX();
            float cy = b.centerY();

            canvas.drawArc(cx, cy, Math.min(cx, cy) - dp(2),
                    startAngle, holder.getValue() * 360F / max, paint);
        }
    }
}

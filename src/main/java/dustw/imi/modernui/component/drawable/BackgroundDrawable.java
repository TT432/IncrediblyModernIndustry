package dustw.imi.modernui.component.drawable;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import static icyllis.modernui.view.View.dp;

/**
 * @author DustW
 **/
public class BackgroundDrawable extends Drawable {
    final int radius;
    @Getter
    @Setter
    int color;

    @Setter
    boolean isArc;

    public BackgroundDrawable(int radius, int color) {

        this.radius = dp(radius);
        this.color = color;
    }

    public BackgroundDrawable(int radius) {
        this(radius, 0xFF295E8A);
    }

    public BackgroundDrawable() {
        this(8);
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        float stroke = radius * 0.25f;

        Paint paint = Paint.take();
        paint.setRGBA(0, 0, 0, 180);

        drawShape(canvas, paint);

        paint.setStyle(Paint.STROKE);
        paint.setStrokeWidth(stroke);
        paint.setColor(color);

        drawShape(canvas, paint);
    }

    public void drawShape(Canvas canvas, Paint paint) {
        Rect b = getBounds();
        float stroke = radius * 0.25f;
        float start = stroke * 0.5f;

        if (!isArc) {
            canvas.drawRoundRect(b.left + start, b.top + start, b.right - start, b.bottom - start, radius, paint);
        }
        else {
            canvas.drawArc(b.centerX(), b.centerY(), Math.min(b.centerX(), b.centerY()) - dp(2), 0, 360, paint);
        }
    }
}

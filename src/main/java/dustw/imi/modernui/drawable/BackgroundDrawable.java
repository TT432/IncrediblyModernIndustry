package dustw.imi.modernui.drawable;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import org.jetbrains.annotations.NotNull;

/**
 * @author DustW
 **/
public class BackgroundDrawable extends Drawable {
    final int radius;
    final int color = 0xFF295E8A;

    public BackgroundDrawable(int radius) {
        this.radius = View.dp(radius);
    }

    public BackgroundDrawable() {
        this(8);
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        Rect b = getBounds();

        float stroke = radius * 0.25f;
        float start = stroke * 0.5f;

        Paint paint = Paint.take();
        paint.setRGBA(0, 0, 0, 180);
        canvas.drawRoundRect(b.left + start, b.top + start, b.right - start, b.bottom - start, radius, paint);
        paint.setStyle(Paint.STROKE);
        paint.setStrokeWidth(stroke);
        paint.setColor(color);
        canvas.drawRoundRect(b.left + start, b.top + start, b.right - start, b.bottom - start, radius, paint);
    }
}

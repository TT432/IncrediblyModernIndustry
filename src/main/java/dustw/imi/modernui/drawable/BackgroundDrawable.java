package dustw.imi.modernui.drawable;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author DustW
 **/
public class BackgroundDrawable extends Drawable {
    final int radius;
    @Getter
    final int color;

    public BackgroundDrawable(int radius, int color) {

        this.radius = View.dp(radius);
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

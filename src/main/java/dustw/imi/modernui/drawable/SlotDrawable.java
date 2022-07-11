package dustw.imi.modernui.drawable;

import icyllis.modernui.forge.CanvasForge;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.view.Gravity;
import lombok.AllArgsConstructor;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import static icyllis.modernui.view.View.dp;

/**
 * @author DustW
 **/
@AllArgsConstructor
public class SlotDrawable extends Drawable {
    final BackgroundDrawable backgroundDrawable = new BackgroundDrawable(4);
    final int size = dp(32);

    Slot slot;

    @Override
    public void draw(@NotNull Canvas canvas) {
        Rect b = getBounds();

        backgroundDrawable.setBounds(b);
        backgroundDrawable.draw(canvas);

        Paint paint = Paint.get();

        CanvasForge.get(canvas).drawItemStack(slot.getItem(),
                b.left + b.centerX(),
                b.top + b.centerY(),
                size, paint);

        if (!slot.getItem().isEmpty() && slot.getItem().getCount() > 1) {
            TextPaint textPaint = new TextPaint();

            textPaint.setColor(0xFFFFFFFF);
            textPaint.setFontSize(dp(16));
            String amount = String.valueOf(slot.getItem().getCount());

            canvas.drawText(amount, 0, amount.length(),
                    b.right - dp(2), b.bottom - dp(3),
                    Gravity.RIGHT, textPaint);
        }
    }
}

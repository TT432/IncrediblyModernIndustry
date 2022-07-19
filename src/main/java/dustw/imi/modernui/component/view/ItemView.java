package dustw.imi.modernui.component.view;

import icyllis.modernui.forge.CanvasForge;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.widget.TextView;
import lombok.AllArgsConstructor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author DustW
 */
@AllArgsConstructor
public class ItemView extends TextView {
    ItemStack itemStack;
    final int size = dp(32);

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);

        int centerX = (getRight() - getLeft()) / 2;
        int centerY = (getBottom() - getTop()) / 2;

        Paint paint = Paint.take();

        if (!itemStack.isEmpty() && !itemStack.hasFoil()) {
            CanvasForge.get(canvas).drawItemStack(itemStack,
                    getLeft() + centerX,
                    getTop() + centerY,
                    size, paint);
        }

        if (!itemStack.isEmpty() && itemStack.getCount() > 1) {
            TextPaint textPaint = new TextPaint();

            textPaint.setColor(0xFFFFFFFF);
            textPaint.setFontSize(dp(16));
            String amount = String.valueOf(itemStack.getCount());

            canvas.drawText(amount, 0, amount.length(),
                    getRight() - dp(2), getBottom() - dp(3),
                    Gravity.RIGHT, textPaint);
        }
    }
}

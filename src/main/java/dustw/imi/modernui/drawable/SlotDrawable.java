package dustw.imi.modernui.drawable;

import icyllis.modernui.forge.CanvasForge;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import lombok.AllArgsConstructor;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

/**
 * @author DustW
 **/
@AllArgsConstructor
public class SlotDrawable extends Drawable {
    final BackgroundDrawable backgroundDrawable = new BackgroundDrawable(4);
    final int size = View.dp(28);

    Slot itemSlot;

    @Override
    public void draw(@NotNull Canvas canvas) {
        Rect b = getBounds();

        backgroundDrawable.setBounds(b);
        backgroundDrawable.draw(canvas);

        Paint paint = Paint.get();

        CanvasForge.get(canvas).drawItemStack(itemSlot.getItem(),
                b.left + b.centerX(),
                b.top + b.centerY(),
                size, paint);
    }
}

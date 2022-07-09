package dustw.imi.modernui.button;

import dustw.imi.client.HoverHandler;
import dustw.imi.modernui.drawable.SlotDrawable;
import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.PropertyValuesHolder;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.IntProperty;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.RadioButton;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

/**
 * @author DustW
 **/
public class SlotButton extends RadioButton {
    final int radius = View.dp(4);

    int alpha;

    protected static final IntProperty<SlotButton> ALPHA_PROPERTY = new IntProperty<>() {
        @Override
        public void setValue(@NotNull SlotButton object, int value) {
            object.alpha = value;
            object.invalidate();
        }

        @Override
        public Integer get(@NotNull SlotButton object) {
            return object.alpha;
        }
    };

    private final Animator mMagAnim;
    private final Animator mMinAnim;

    Slot slot;
    SlotDrawable drawable;
    TextPaint paint;

    public SlotButton(Slot slot, SlotDrawable drawable, TextPaint paint) {
        this.slot = slot;
        this.drawable = drawable;
        this.paint = paint;

        mMagAnim = ObjectAnimator.ofPropertyValuesHolder(this,
                PropertyValuesHolder.ofInt(ALPHA_PROPERTY, 0, 100));
        mMagAnim.setInterpolator(TimeInterpolator.ACCELERATE);

        mMinAnim = ObjectAnimator.ofPropertyValuesHolder(this,
                PropertyValuesHolder.ofInt(ALPHA_PROPERTY, 100, 0));
        mMinAnim.setInterpolator(TimeInterpolator.DECELERATE);
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);

        float stroke = radius * 0.25f;
        float start = stroke * 0.5f;

        var b = drawable.getBounds();

        Paint paint = Paint.take();
        paint.setRGBA(255, 255, 255, ALPHA_PROPERTY.get(this));
        canvas.drawRoundRect(b.left + start, b.top + start, b.right - start, b.bottom - start, radius, paint);

        if (!slot.getItem().isEmpty() && slot.getItem().getCount() > 1) {
            this.paint.setColor(0xFFFFFFFF);
            this.paint.setFontSize(32);
            String amount = String.valueOf(slot.getItem().getCount());

            canvas.drawText(amount, 0, amount.length(),
                    b.right - dp(2), b.bottom - dp(3),
                    Gravity.RIGHT, this.paint);

            this.paint.setColor(0);
        }
    }

    @Override
    public void onHoverChanged(boolean hovered) {
        super.onHoverChanged(hovered);

        Core.postOnRenderThread(() -> {
            HoverHandler.hovered = hovered;
            HoverHandler.slot = hovered ? slot : null;
        });

        if (hovered) {
            mMinAnim.cancel();
            mMagAnim.setupStartValues();
            mMagAnim.start();
        } else {
            mMagAnim.cancel();
            mMinAnim.setupStartValues();
            mMinAnim.start();
        }
    }
}

package dustw.imi.modernui.component.button;

import dustw.imi.client.HoverHandler;
import dustw.imi.modernui.component.drawable.BackgroundDrawable;
import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.PropertyValuesHolder;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.core.Core;
import icyllis.modernui.forge.CanvasForge;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.math.Rect;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.IntProperty;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.RadioButton;
import lombok.Getter;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

/**
 * @author DustW
 **/
public class SlotButton extends RadioButton {
    final int radius = View.dp(4);
    @Getter
    final BackgroundDrawable backgroundDrawable = new BackgroundDrawable(4);
    final int size = dp(32);

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

    public SlotButton(Slot slot) {
        this.slot = slot;

        setBackground(backgroundDrawable);

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

        Paint paint = Paint.take();
        paint.setRGBA(255, 255, 255, ALPHA_PROPERTY.get(this));
        backgroundDrawable.drawShape(canvas, paint);

        backgroundDrawable.draw(canvas);

        paint = Paint.get();
        Rect b = backgroundDrawable.getBounds();

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

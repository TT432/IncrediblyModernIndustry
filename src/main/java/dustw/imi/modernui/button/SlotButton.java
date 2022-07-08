package dustw.imi.modernui.button;

import dustw.imi.client.HoverHandler;
import dustw.imi.modernui.drawable.SlotDrawable;
import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.PropertyValuesHolder;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.util.IntProperty;
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

    public SlotButton(Slot slot, SlotDrawable drawable) {
        this.slot = slot;
        this.drawable = drawable;

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
    }

    @Override
    public void onHoverChanged(boolean hovered) {
        super.onHoverChanged(hovered);

        HoverHandler.hovered = hovered;

        if (hovered) {
            HoverHandler.slot = slot;
        }
        else {
            HoverHandler.slot = null;
        }

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

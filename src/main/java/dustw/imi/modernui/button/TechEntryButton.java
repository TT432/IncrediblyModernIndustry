package dustw.imi.modernui.button;

import dustw.imi.modernui.drawable.BackgroundDrawable;
import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.PropertyValuesHolder;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.math.PointF;
import icyllis.modernui.util.IntProperty;
import icyllis.modernui.widget.ImageButton;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * @author DustW
 **/
public class TechEntryButton extends ImageButton {

    @Setter
    BackgroundDrawable bounding;
    @Setter
    boolean select;
    @Setter
    TechEntryButton parent;
    int alpha;

    private final int radius = dp(8);

    private final Animator mMagAnim;
    private final Animator mMinAnim;

    public static final IntProperty<TechEntryButton> ALPHA_PROPERTY = new IntProperty<>() {
        @Override
        public void setValue(@NotNull TechEntryButton object, int value) {
            object.alpha = value;
            object.invalidate();
        }

        @Override
        public Integer get(@NotNull TechEntryButton object) {
            return object.alpha;
        }
    };

    public TechEntryButton() {
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

        var b = bounding.getBounds();

        if (parent != null) {
            drawLines(canvas);
        }

        Paint paint = Paint.take();
        paint.setRGBA(255, 255, 255, ALPHA_PROPERTY.get(this));
        canvas.drawRoundRect(b.left + start, b.top + start, b.right - start, b.bottom - start, radius, paint);

        if (select) {
            paint.setStyle(Paint.STROKE);
            float inner = radius * .2F;
            paint.setStrokeWidth(inner * 2);
            paint.setColor(0XFFF39C12);
            canvas.drawRoundRect(inner, inner, getWidth() - inner, getHeight() - inner, radius, paint);
        }
    }

    void drawLines(Canvas canvas) {
        // TODO 修复画线的问题

        var b = bounding.getBounds();
        Paint paint = Paint.take();
        paint.setColor(bounding.getColor());
        paint.setStyle(Paint.STROKE);
        paint.setStrokeWidth(dp(2));

        float x = parent.getLeft() - getLeft();

        PointF selfTop = new PointF(b.centerX(), b.top);
        float centerDis = parent.getBottom() - getTop();
        PointF parentBottom = new PointF(x, b.top - centerDis);
        float centerY = b.top - centerDis / 2;
        PointF selfCenterPoint = new PointF(selfTop.x, centerY);
        PointF parentCenterPoint = new PointF(parentBottom.x, centerY);

        drawLine(canvas, selfTop, selfCenterPoint, paint);
        drawLine(canvas, selfCenterPoint, parentCenterPoint, paint);
        //drawLine(canvas, parentBottom, parentCenterPoint, paint);
    }

    void drawLine(Canvas canvas, PointF point1, PointF point2, Paint paint) {
        canvas.drawRoundLine(point1.x, point1.y, point2.x, point2.y, paint);
    }

    @Override
    public void onHoverChanged(boolean hovered) {
        super.onHoverChanged(hovered);

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

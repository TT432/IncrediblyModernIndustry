package dustw.imi.modernui.component.view;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.FMath;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.EdgeEffect;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.OverScroller;

import javax.annotation.Nonnull;

/**
 * @author DustW
 **/
public class HorizontalScrollView extends FrameLayout {

    private final OverScroller mScroller;

    private final int mTouchSlop;

    private final int mOverscrollDistance;

    private final int mMaximumVelocity;
    private final int mMinimumVelocity;

    private final float mVerticalScrollFactor;

    /** Used during scrolling to retrieve the new offset within the window. */
    private final int[] mScrollConsumed = new int[2];
    private final int[] mScrollOffset = new int[2];

    /**
     * Tracks the state of the top edge glow.
     * <p>
     * Even though this field is practically final, we cannot make it final because there are apps
     * setting it via reflection and they need to keep working until they target Q.
     */
    private final EdgeEffect mEdgeGlowLeft;

    /**
     * Tracks the state of the bottom edge glow.
     * <p>
     * Even though this field is practically final, we cannot make it final because there are apps
     * setting it via reflection and they need to keep working until they target Q.
     */
    private final EdgeEffect mEdgeGlowRight;

    private final Rect mTempRect = new Rect();


    private int mNestedXOffset;
    private VelocityTracker mVelocityTracker;
    private int mLastMotionX;
    private boolean mIsBeingDragged;

    /**
     * The child to give focus to in the event that a child has requested focus while the
     * layout is dirty. This prevents the scroll from being wrong if the child has not been
     * laid out before requesting focus.
     */
    private View mChildToScrollTo = null;

    /**
     * When set to true, the scroll view measure its child to make it fill the currently
     * visible area.
     */
    private boolean mFillViewport;
    private final int mOverflingDistance;
    private boolean mIsLayoutDirty;


    public HorizontalScrollView() {
        mScroller = new OverScroller();

        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);

        mEdgeGlowLeft = new EdgeEffect();
        mEdgeGlowRight = new EdgeEffect();

        final ViewConfiguration configuration = ViewConfiguration.get();
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        mOverflingDistance = configuration.getScaledOverflingDistance();
        mVerticalScrollFactor = configuration.getScaledVerticalScrollFactor();

        setHorizontalScrollBarEnabled(true);
        setHorizontalScrollbarThumbDrawable(new Drawable() {
            private int mAlpha = 255;

            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                paint.setRGBA(84, 190, 196, (int) (mAlpha * 0.5));
                Rect bounds = getBounds();
                canvas.drawRoundRect(bounds.left + 1, bounds.top + 1, bounds.right - 1, bounds.bottom - 1,
                        bounds.width() / 2f - 1, paint);
            }

            @Override
            public void setAlpha(int alpha) {
                this.mAlpha = alpha;
            }
        });
        setHorizontalScrollbarTrackDrawable(new Drawable() {
            private int mAlpha = 255;

            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                paint.setRGBA(128, 128, 128, (int) (mAlpha * 0.75));
                paint.setStyle(Paint.STROKE);
                paint.setStrokeWidth(3);
                Rect bounds = getBounds();
                canvas.drawRoundRect(bounds.left + 1, bounds.top + 1, bounds.right - 1, bounds.bottom - 1,
                        bounds.width() / 2f - 1, paint);
            }

            @Override
            public void setAlpha(int alpha) {
                this.mAlpha = alpha;
            }
        });
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(@Nonnull MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        if (super.onInterceptTouchEvent(ev)) {
            return true;
        }

        if (getScrollX() == 0 && !canScrollHorizontally(1)) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE -> {
                final int x = (int) ev.getX();
                final int xDiff = Math.abs(x - mLastMotionX);
                if (xDiff > mTouchSlop && (getNestedScrollAxes() & SCROLL_AXIS_HORIZONTAL) == 0) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    mNestedXOffset = 0;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
            }
            case MotionEvent.ACTION_DOWN -> {
                final int x = (int) ev.getX();
                if (!inChild(x, (int) ev.getY())) {
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                    break;
                }

                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
                mLastMotionX = x;

                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                /*
                 * If being flinged and user touches the screen, initiate drag;
                 * otherwise don't. mScroller.isFinished should be false when
                 * being flinged. We need to call computeScrollOffset() first so that
                 * isFinished() is correct.
                 */
                mScroller.computeScrollOffset();
                mIsBeingDragged = !mScroller.isFinished() || !mEdgeGlowRight.isFinished()
                        || !mEdgeGlowLeft.isFinished();
                // Catch the edge effect if it is active.
                if (!mEdgeGlowLeft.isFinished()) {
                    mEdgeGlowLeft.onPullDistance(0, ev.getY() / getHeight());
                }
                if (!mEdgeGlowRight.isFinished()) {
                    mEdgeGlowRight.onPullDistance(0, 1f - ev.getY() / getHeight());
                }
                startNestedScroll(SCROLL_AXIS_HORIZONTAL, TYPE_TOUCH);
            }
            case MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                /* Release the drag */
                mIsBeingDragged = false;
                recycleVelocityTracker();
                if (mScroller.springBack(mScrollX, mScrollY, 0, getScrollRange(), 0, 0)) {
                    postInvalidateOnAnimation();
                }
                stopNestedScroll(TYPE_TOUCH);
            }
            default -> {}
        }

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@Nonnull MotionEvent ev) {
        initVelocityTrackerIfNotExists();

        MotionEvent vtev = ev.copy();

        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mNestedXOffset = 0;
        }
        vtev.offsetLocation(mNestedXOffset, 0);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (getChildCount() == 0) {
                    vtev.recycle();
                    return false;
                }
                if (!mScroller.isFinished()) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                // Remember where the motion event started
                mLastMotionX = (int) ev.getX();
                startNestedScroll(SCROLL_AXIS_HORIZONTAL, TYPE_TOUCH);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int x = (int) ev.getX();
                int deltaX = mLastMotionX - x;
                if (dispatchNestedPreScroll(deltaX, 0, mScrollConsumed, mScrollOffset, TYPE_TOUCH)) {
                    deltaX -= mScrollConsumed[0];
                    vtev.offsetLocation(mScrollOffset[0], 0);
                    mNestedXOffset += mScrollOffset[0];
                }
                if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionX = x - mScrollOffset[0];

                    final int oldX = mScrollX;
                    final int range = getScrollRange();
                    final int overscrollMode = getOverScrollMode();
                    boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                            (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                    final float displacement = ev.getY() / getHeight();

                    if (canOverscroll) {
                        int consumed = 0;
                        if (deltaX < 0 && mEdgeGlowLeft.getDistance() != 0f) {
                            consumed = Math.round(getWidth()
                                    * mEdgeGlowRight.onPullDistance((float) deltaX / getWidth(),
                                    1 - displacement));
                        } else if (deltaX > 0 && mEdgeGlowLeft.getDistance() != 0f) {
                            consumed = Math.round(-getWidth()
                                    * mEdgeGlowRight.onPullDistance((float) -deltaX / getWidth(),
                                    displacement));
                        }
                        deltaX -= consumed;
                    }

                    // Calling overScrollBy will call onOverScrolled, which
                    // calls onScrollChanged if applicable.
                    if (overScrollBy(deltaX, 0, mScrollX, 0,
                            range, 0, mOverscrollDistance, 0, true)
                            && !hasNestedScrollingParent(TYPE_TOUCH)) {
                        // Break our velocity if we hit a scroll barrier.
                        mVelocityTracker.clear();
                    }

                    final int scrolledDeltaX = mScrollX - oldX;
                    final int unconsumedX = deltaX - scrolledDeltaX;
                    if (dispatchNestedScroll(scrolledDeltaX, 0, unconsumedX, 0, mScrollOffset, TYPE_TOUCH,
                            mScrollConsumed)) {
                        mLastMotionX -= mScrollOffset[0];
                        vtev.offsetLocation(mScrollOffset[0], 0);
                        mNestedXOffset += mScrollOffset[0];
                    } else if (canOverscroll && deltaX != 0f) {
                        final int pulledToX = oldX + deltaX;
                        if (pulledToX < 0) {
                            mEdgeGlowLeft.onPullDistance((float) -deltaX / getWidth(),
                                    displacement);
                            if (!mEdgeGlowRight.isFinished()) {
                                mEdgeGlowRight.onRelease();
                            }
                        } else if (pulledToX > range) {
                            mEdgeGlowRight.onPullDistance((float) deltaX / getWidth(),
                                    1.f - displacement);
                            if (!mEdgeGlowLeft.isFinished()) {
                                mEdgeGlowLeft.onRelease();
                            }
                        }
                        if (shouldDisplayEdgeEffects()
                                && (!mEdgeGlowLeft.isFinished() || !mEdgeGlowRight.isFinished())) {
                            postInvalidateOnAnimation();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity();

                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        flingWithNestedDispatch(-initialVelocity);
                    } else if (mScroller.springBack(mScrollX, mScrollY, 0, getScrollRange(), 0, 0)) {
                        postInvalidateOnAnimation();
                    }

                    endDrag();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {
                    if (mScroller.springBack(mScrollX, mScrollY, 0, getScrollRange(), 0, 0)) {
                        postInvalidateOnAnimation();
                    }
                    endDrag();
                }
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    @Override
    public boolean onGenericMotionEvent(@Nonnull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_SCROLL) {
            final float axisValue = event.getAxisValue(MotionEvent.AXIS_HSCROLL);
            final int delta = Math.round(axisValue * mVerticalScrollFactor);
            if (Math.abs(axisValue) > 0.9 && Math.abs(delta) * 6 > mMinimumVelocity) {
                int deltaX = FMath.clamp(delta * 6, -mMaximumVelocity, mMaximumVelocity);
                flingWithNestedDispatch(-deltaX);
                return true;
            } else if (smoothScrollBy(-delta)) {
                return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        // Treat animating scrolls differently; see #computeScroll() for why.
        if (!mScroller.isFinished()) {
            final int oldX = mScrollX;
            final int oldY = mScrollY;
            mScrollX = scrollX;
            mScrollY = scrollY;
            onScrollChanged(mScrollX, mScrollY, oldX, oldY);
            if (clampedY) {
                mScroller.springBack(mScrollX, mScrollY, 0, getScrollRange(), 0, 0);
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
        awakenScrollBars();
    }

    @Override
    protected int computeHorizontalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = getWidth() - mPaddingRight - mPaddingLeft;
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = getAllChildLen();
        final int scrollX = mScrollX;
        final int overscrollRight = Math.max(0, scrollRange - contentHeight);
        if (scrollX < 0) {
            scrollRange -= scrollX;
        } else if (scrollX > overscrollRight) {
            scrollRange += scrollX - overscrollRight;
        }

        return scrollRange;
    }

    @Override
    protected int computeHorizontalScrollOffset() {
        return Math.max(0, super.computeHorizontalScrollOffset());
    }

    @Override
    protected void measureChild(@Nonnull View child, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();

        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, mPaddingTop
                + mPaddingBottom, lp.height);
        final int hPadding = mPaddingLeft + mPaddingRight;
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - hPadding),
                MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(@Nonnull View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);
        final int usedTotal = mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin +
                widthUsed;
        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - usedTotal),
                MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We don't want to
            // re-show the scrollbars at this point, which scrollTo will do,
            // so we replicate most of scrollTo here.
            //
            //         It's a little odd to call onScrollChanged from inside the drawing.
            //
            //         It is, except when you remember that computeScroll() is used to
            //         animate scrolling. So unless we want to defer the onScrollChanged()
            //         until the end of the animated scrolling, we don't really have a
            //         choice here.
            //
            //         I agree.  The alternative, which I think would be worse, is to post
            //         something and tell the subclasses later.  This is bad because there
            //         will be a window where mScrollX/Y is different from what the app
            //         thinks it is.
            //
            int oldX = mScrollX;
            int oldY = mScrollY;
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                final int range = getScrollRange();
                final int overscrollMode = getOverScrollMode();
                final boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                        (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                overScrollBy(x - oldX, y - oldY, oldX, oldY, range, 0,
                        mOverflingDistance, 0, false);
                onScrollChanged(mScrollX, mScrollY, oldX, oldY);

                if (canOverscroll) {
                    if (x < 0 && oldX >= 0) {
                        mEdgeGlowLeft.onAbsorb((int) mScroller.getCurrVelocity());
                    } else if (x > range && oldX <= range) {
                        mEdgeGlowRight.onAbsorb((int) mScroller.getCurrVelocity());
                    }
                }
            }

            if (!awakenScrollBars()) {
                // Keep on drawing until the animation has finished.
                postInvalidateOnAnimation();
            }
        }
    }

    @Override
    public boolean requestChildRectangleOnScreen(@Nonnull View child, @Nonnull Rect rectangle,
                                                 boolean immediate) {
        // offset into coordinate space of this scroll view
        rectangle.offset(child.getLeft() - child.getScrollX(),
                child.getTop() - child.getScrollY());

        return scrollToChildRect(rectangle, immediate);
    }

    @Override
    public void requestLayout() {
        mIsLayoutDirty = true;
        super.requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mIsLayoutDirty = false;
        // Give a child focus if it needs it
        if (mChildToScrollTo != null && isViewDescendantOf(mChildToScrollTo, this)) {
            scrollToDescendant(mChildToScrollTo);
        }
        mChildToScrollTo = null;

        if (!isLaidOut()) {
            final int childHeight = (getChildCount() > 0) ? getChildAt(0).getMeasuredHeight() : 0;
            final int scrollRange = Math.max(0,
                    childHeight - (b - t - mPaddingBottom - mPaddingTop));

            // Don't forget to clamp
            if (mScrollY > scrollRange) {
                mScrollY = scrollRange;
            } else if (mScrollY < 0) {
                mScrollY = 0;
            }
        }

        // Calling this with the present values causes it to re-claim them
        scrollTo(mScrollX, mScrollY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int prevWidth, int prevHeight) {
        super.onSizeChanged(w, h, prevWidth, prevHeight);

        View currentFocused = findFocus();
        if (currentFocused == null || currentFocused == this) {
            return;
        }

        // If the currently-focused view was visible on the screen when the
        // screen was at the old height, then scroll the screen to make that
        // view visible with the new screen height.
        if (isWithinDeltaOfScreen(currentFocused, 0, prevWidth)) {
            currentFocused.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(currentFocused, mTempRect);
            int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
            if (scrollDelta != 0) {
                smoothScrollBy(scrollDelta);
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(@Nonnull View child, @Nonnull View target, int axes, int type) {
        return (axes & SCROLL_AXIS_HORIZONTAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@Nonnull View child, @Nonnull View target, int axes, int type) {
        super.onNestedScrollAccepted(child, target, axes, type);
        startNestedScroll(SCROLL_AXIS_HORIZONTAL, type);
    }

    @Override
    public void onNestedScroll(@Nonnull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type, @Nonnull int[] consumed) {
        final int oldScrollX = mScrollX;
        scrollBy(dxConsumed, 0);
        final int mxConsumed = mScrollX - oldScrollX;
        final int mxUnconsumed = dxUnconsumed - mxConsumed;
        consumed[0] += mxConsumed;
        dispatchNestedScroll(mxConsumed, 0, mxUnconsumed, 0, null, type, consumed);
    }

    @Override
    public boolean onNestedFling(@Nonnull View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            flingWithNestedDispatch((int) velocityX);
            return true;
        }
        return false;
    }

    @Override
    public void onDrawForeground(@Nonnull Canvas canvas) {
        super.onDrawForeground(canvas);
        if (shouldDisplayEdgeEffects()) {
            final int scrollX = mScrollX;
            final boolean clipToPadding = getClipToPadding();
            if (!mEdgeGlowLeft.isFinished()) {
                canvas.save();
                final int width;
                final int height;
                final float translateX;
                final float translateY;
                if (clipToPadding) {
                    width = getWidth() - mPaddingLeft - mPaddingRight;
                    height = getHeight() - mPaddingTop - mPaddingBottom;
                    translateX = mPaddingLeft;
                    translateY = mPaddingTop;
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0;
                    translateY = 0;
                }
                canvas.translate(Math.min(0, scrollX) + translateX, translateY);
                canvas.rotate(270, width / 2F, height / 2F);
                mEdgeGlowLeft.setSize(width, height);
                if (mEdgeGlowLeft.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restore();
            }
            if (!mEdgeGlowRight.isFinished()) {
                canvas.save();
                final int width;
                final int height;
                final float translateX;
                final float translateY;
                if (clipToPadding) {
                    width = getWidth() - mPaddingLeft - mPaddingRight;
                    height = getHeight() - mPaddingTop - mPaddingBottom;
                    translateX = mPaddingLeft;
                    translateY = mPaddingTop;
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0;
                    translateY = 0;
                }
                canvas.translate(Math.max(getScrollRange(), scrollX) + translateX, translateY);
                canvas.rotate(90, width / 2F, height / 2F);
                mEdgeGlowRight.setSize(width, height);
                if (mEdgeGlowRight.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restore();
            }
        }
    }

    private boolean isWithinDeltaOfScreen(@Nonnull View descendant, int delta, int width) {
        descendant.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(descendant, mTempRect);

        return (mTempRect.right + delta) >= getScrollX()
                && (mTempRect.left - delta) <= (getScrollX() + width);
    }

    private int getScrollRange() {
        return Math.max(0, getAllChildLen() - (getWidth() - mPaddingRight - mPaddingLeft));
    }

    private int getAllChildLen() {
        int len = 0;

        for (int i = 0; i < getChildCount(); i++) {
            len += getChildAt(i).getWidth();
        }

        return len;
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private boolean shouldDisplayEdgeEffects() {
        return getOverScrollMode() != OVER_SCROLL_NEVER;
    }

    private void flingWithNestedDispatch(int velocityX) {
        final boolean canFling = (mScrollX > 0 || velocityX > 0) &&
                (mScrollX < getScrollRange() || velocityX < 0);
        if (!dispatchNestedPreFling(velocityX, 0)) {
            final boolean consumed = dispatchNestedFling(velocityX, 0, canFling);
            if (canFling) {
                fling(velocityX);
            } else if (!consumed) {
                if (!mEdgeGlowLeft.isFinished()) {
                    mEdgeGlowLeft.onAbsorb(-velocityX);
                } else if (!mEdgeGlowRight.isFinished()) {
                    mEdgeGlowRight.onAbsorb(velocityX);
                }
            }
        }
    }

    public void fling(int velocityX) {
        if (getChildCount() > 0) {
            int width = getWidth() - mPaddingRight - mPaddingLeft;
            int right = getAllChildLen();

            mScroller.fling(mScrollX, mScrollY, velocityX, 0, 0,
                    Math.max(0, right - width), 0, 0, width / 2, 0);

            postInvalidateOnAnimation();
        }
    }

    private void endDrag() {
        mIsBeingDragged = false;

        recycleVelocityTracker();

        if (shouldDisplayEdgeEffects()) {
            mEdgeGlowLeft.onRelease();
            mEdgeGlowRight.onRelease();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollX = mScrollX;
            final View child = getChildAt(0);
            return !(x < child.getRight() - scrollX
                    || x >= child.getLeft() - scrollX
                    || y < child.getTop()
                    || y >= child.getBottom());
        }
        return false;
    }

    public final boolean smoothScrollBy(int delta) {
        if (getChildCount() == 0) {
            // Nothing to do.
            return false;
        }
        delta = Math.max(0, Math.min(mScroller.getFinalX() + delta, getScrollRange())) - mScrollX;
        if (delta != 0) {
            mScroller.startScroll(mScrollX, mScrollY, delta, 0);
            postInvalidateOnAnimation();
            return true;
        }
        return false;
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        final int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
        if (delta != 0) {
            if (immediate) {
                scrollBy(delta, 0);
            } else {
                smoothScrollBy(delta);
            }
            return true;
        }
        return false;
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) return 0;

        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;

        int scrollXDelta = 0;

        if (rect.right > screenRight && rect.left > screenLeft) {
            // need to move down to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.width() > width) {
                // just enough to get screen size chunk on
                scrollXDelta += (rect.left - screenLeft);
            } else {
                // get entire rect at bottom of screen
                scrollXDelta += (rect.right - screenRight);
            }

            // make sure we aren't scrolling beyond the end of our content
            int right = getAllChildLen();
            int distanceToRight = right - screenRight;
            scrollXDelta = Math.min(scrollXDelta, distanceToRight);

        } else if (rect.left < screenLeft && rect.right < screenRight) {
            // need to move up to get it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.width() > width) {
                // screen size chunk
                scrollXDelta -= (screenRight - rect.right);
            } else {
                // entire rect at top
                scrollXDelta -= (screenLeft - rect.left);
            }

            // make sure we aren't scrolling any further than the top our content
            scrollXDelta = Math.max(scrollXDelta, -getScrollX());
        }
        return scrollXDelta;
    }

    private static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        }

        final ViewParent theParent = child.getParent();
        return (theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent);
    }

    public void scrollToDescendant(@Nonnull View child) {
        if (!mIsLayoutDirty) {
            child.getDrawingRect(mTempRect);

            /* Offset from child's local coordinates to ScrollView coordinates */
            offsetDescendantRectToMyCoords(child, mTempRect);

            int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);

            if (scrollDelta != 0) {
                scrollBy(0, scrollDelta);
            }
        } else {
            mChildToScrollTo = child;
        }
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- mScrollX --|
             */
            return 0;
        }
        if ((my + n) > child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
            return child - my;
        }
        return n;
    }
}

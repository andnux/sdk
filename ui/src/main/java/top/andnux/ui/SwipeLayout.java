package top.andnux.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

/**
 * @desc: 用来做侧滑使用
 */
public class SwipeLayout extends LinearLayout {

    private static final int DURATION = 200;
    private OverScroller mScroller;
    //内容的ViewGroup
    private View mContent;
    //右侧menu
    private View mMenuContent;
    //速度计算器
    private VelocityTracker mVelocityTracker;
    //能计算出的fling速度
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private float curX;
    private boolean mIsBeingDragged = true;

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId = ViewDragHelper.INVALID_POINTER;
    /**
     * 上一次motionEvent的x位置
     */
    private int mLastMotionX;

    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(getContext(), new FastOutLinearInInterpolator());
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthParentMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        if (getChildCount() == 2) {
            if (mContent == null || mMenuContent == null) {
                mContent = getChildAt(0);
                mMenuContent = getChildAt(1);
            }
        } else if (getChildCount() == 1) {
            if (mContent == null) {
                mContent = getChildAt(0);
            }
        }
        measureChildWithMargins(mContent,
                MeasureSpec.makeMeasureSpec(widthParentMeasureSize, MeasureSpec.EXACTLY),
                0, heightMeasureSpec, 0);
        measureChildWithMargins(mMenuContent,
                widthMeasureSpec, 0, MeasureSpec.makeMeasureSpec(mContent.getMeasuredHeight(),
                        MeasureSpec.EXACTLY), 0);

        setMeasuredDimension(mContent.getMeasuredWidth(),
                mContent.getMeasuredHeight());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                resetTouchState();
                /**
                 * 多点触摸计算
                 */
                mActivePointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();
                int measuredWidth = mMenuContent.getMeasuredWidth();
                float moveX = event.getX();
                int distanceX = (int) (curX - moveX);
                if (distanceX + scrollX < 0) {
                    distanceX = 0 - scrollX;
                }
                if (distanceX + scrollX > measuredWidth) {
                    distanceX = measuredWidth - scrollX;
                }
                if (curX != 0)
                    scrollBy(distanceX, 0);
                curX = moveX;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetTouchState();
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);

                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    fling(-initialVelocity);
                }
                swipeOrOpenMenu(-initialVelocity);
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    private void swipeOrOpenMenu(int velocity) {
        int scrollX = getScrollX();
        int measuredWidth = mMenuContent.getMeasuredWidth();
        int halfScrollWidth = measuredWidth / 2;
        if (Math.abs(velocity) <= mMinimumVelocity) {
            if (scrollX >= halfScrollWidth) {
                smoothOpenMenu(DURATION);
            } else {
                smoothCloseMenu(DURATION);
            }
        } else {
            if (velocity < 0) {
                smoothCloseMenu(DURATION);
            } else {
                smoothOpenMenu(DURATION);
            }
        }

    }

    private OnSwipeStateChangeListener onSwipeStateChangeListener;

    private void smoothOpenMenu(int duration) {
        if (onSwipeStateChangeListener != null) onSwipeStateChangeListener.onSwipeStateChange(true);
        if (!mScroller.isFinished())
            mScroller.forceFinished(true);
        int scrollX = getScrollX();
        mScroller.startScroll(Math.abs(scrollX), 0,
                mMenuContent.getMeasuredWidth() - Math.abs(scrollX), 0, duration);
        invalidate();
    }

    private void smoothCloseMenu(int duration) {
        if (onSwipeStateChangeListener != null)
            onSwipeStateChangeListener.onSwipeStateChange(false);
        if (!mScroller.isFinished())
            mScroller.forceFinished(true);
        int scrollX = getScrollX();
        mScroller.startScroll(Math.abs(scrollX), 0,
                -Math.abs(scrollX), 0, duration);
        invalidate();
    }

    private void resetTouchState() {
        curX = 0;
    }

    private void fling(int xInitialVelocity) {
        if (getChildCount() > 0) {
            mScroller.fling(getScrollX(), getScrollY(), xInitialVelocity, 0, 0,
                    mMenuContent.getMeasuredWidth(),
                    0, 0);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        if (super.onInterceptTouchEvent(ev)) {
            return true;
        }


        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == ViewDragHelper.INVALID_POINTER) {
                    break;
                }

                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

                final int x = (int) ev.getX(pointerIndex);
                final int xDiff = Math.abs(x - mLastMotionX);
                if (xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                mLastMotionX = (int) ev.getX();
                mActivePointerId = ev.getPointerId(0);

                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                /*
                 * If being flinged and user touches the screen, initiate drag;
                 * otherwise don't. mScroller.isFinished should be false when
                 * being flinged. We need to call computeScrollOffset() first so that
                 * isFinished() is correct.
                 */
                mScroller.computeScrollOffset();
                mIsBeingDragged = !mScroller.isFinished();
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = ViewDragHelper.INVALID_POINTER;
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        return mIsBeingDragged;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
                MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = (int) ev.getX(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }


    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollBy(x - oldX, y - oldY);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    public void setOpen(final boolean open) {
        if (open) {
            scrollTo(mMenuContent.getMeasuredWidth(), 0);
        } else {
            scrollTo(0, 0);
        }
    }


    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void setOnSwipeStateChangeListener(OnSwipeStateChangeListener onSwipeStateChangeListener) {
        this.onSwipeStateChangeListener = onSwipeStateChangeListener;
    }

    public interface OnSwipeStateChangeListener {
        void onSwipeStateChange(boolean var1);
    }
}

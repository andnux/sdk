package top.andnux.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.InputStream;

public class BigImageView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener {

    private final Rect mRect;
    private final BitmapFactory.Options mOptions;
    private final GestureDetector mGestureDetector;
    private final Scroller mScroller;
    private float mImageWidth;
    private float mImageHeight;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private float mViewWidth;
    private float mViewHeight;
    private Matrix mMatrix;
    private Bitmap mBitmap;
    private float mScale;

    public BigImageView(Context context) {
        this(context, null);
    }

    public BigImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();
        mGestureDetector = new GestureDetector(context, this);
        mScroller = new Scroller(context);
        mMatrix = new Matrix();
        setOnTouchListener(this);
    }

    public void setImage(InputStream image) throws Exception {
        setImage(image, Bitmap.Config.RGB_565);
    }

    public void setImage(InputStream image, Bitmap.Config config) throws Exception {
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(image, null, mOptions);
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        mOptions.inMutable = true;
        mOptions.inPreferredConfig = config;
        mOptions.inJustDecodeBounds = false;
        mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(image, false);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mScale = mViewWidth / mImageWidth;
        mRect.right = (int) mViewWidth;
        mRect.bottom = (int) (mViewHeight / mScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmapRegionDecoder == null) return;
        mOptions.inBitmap = mBitmap;
        mBitmap = mBitmapRegionDecoder.decodeRegion(mRect, mOptions);
        mMatrix.setScale(mScale, mScale);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        return true;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mRect.offset(0, (int) distanceY);
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = (int) mImageHeight;
            mRect.top = (int) (mImageHeight - mViewWidth / mScale);
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = (int) (mViewHeight / mScale);
        }
        invalidate();
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mScroller.fling(0, mRect.top, 0, (int) -velocityY,
                0, 0, 0, (int) (mImageHeight - (mViewHeight / mScale)));
        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.isFinished()) {
            return;
        }
        if (mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int) (mViewHeight / mScale);
            invalidate();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }
}

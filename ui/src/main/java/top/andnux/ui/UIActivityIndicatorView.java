package top.andnux.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

public class UIActivityIndicatorView extends View {

    private int startColor = Color.argb(255, 255, 255, 255);
    private float strokeWidth = 0;
    private int startAngle = 0;

    private final int mLineCount = 12;
    private final int mMinAlpha = 0;
    private final int mAngleGradient = 360 / mLineCount;
    private Paint paint;
    private int[] colors = new int[mLineCount];

    public UIActivityIndicatorView(Context context) {
        this(context, null);
    }

    public UIActivityIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIActivityIndicatorView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int i1 = typedArray.getIndex(i);
            if (i1 == R.styleable.UIActivityIndicatorView_AIV_startColor) {
                startColor = typedArray.getColor(R.styleable.UIActivityIndicatorView_AIV_startColor, startColor);

            } else if (i1 == R.styleable.UIActivityIndicatorView_AIV_startAngle) {
                startAngle = typedArray.getInt(R.styleable.UIActivityIndicatorView_AIV_startAngle, startAngle);

            } else if (i1 == R.styleable.UIActivityIndicatorView_AIV_strokeWidth) {
                strokeWidth = typedArray.getDimension(R.styleable.UIActivityIndicatorView_AIV_strokeWidth, strokeWidth);

            }
        }
        typedArray.recycle();
        initialize();
    }

    private void initialize() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int alpha = Color.alpha(startColor);
        int red = Color.red(startColor);
        int green = Color.green(startColor);
        int blue = Color.blue(startColor);
        int alpha_gradient = Math.abs(alpha - mMinAlpha) / mLineCount;
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Color.argb(alpha - alpha_gradient * i, red, green, blue);
        }
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        float radius = Math.min(getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom()) * 0.5f;
        if (strokeWidth == 0) strokeWidth = pointX(mAngleGradient / 2, radius / 2) / 2;
        paint.setStrokeWidth(strokeWidth);
        for (int i = 0; i < colors.length; i++) {
            paint.setColor(colors[i]);
            canvas.drawLine(
                    centerX + pointX(-mAngleGradient * i + startAngle, radius / 2),
                    centerY + pointY(-mAngleGradient * i + startAngle, radius / 2),
                    centerX + pointX(-mAngleGradient * i + startAngle, radius - strokeWidth / 2),   //  这里计算Y值时, 之所以减去线宽/2, 是防止没有设置的Padding时,图像会超出View范围
                    centerY + pointY(-mAngleGradient * i + startAngle, radius - strokeWidth / 2),   //  这里计算Y值时, 之所以减去线宽/2, 是防止没有设置的Padding时,图像会超出View范围
                    paint);
        }
    }

    private float pointX(int angle, float radius) {
        return (float) (radius * Math.cos(angle * Math.PI / 180));
    }

    private float pointY(int angle, float radius) {
        return (float) (radius * Math.sin(angle * Math.PI / 180));
    }

    private Handler animHandler = new MyHandler(this);

    static class MyHandler extends Handler {

        private WeakReference<UIActivityIndicatorView> mReference;

        MyHandler(UIActivityIndicatorView reference) {
            mReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UIActivityIndicatorView view = mReference.get();
            if (view == null) {
                return;
            }
            view.startAngle += view.mAngleGradient;
            view.invalidate();
            view.animHandler.sendEmptyMessageDelayed(0x00, 50);
        }
    }

    public void start() {
        if (animHandler == null){
            animHandler = new MyHandler(this);
        }
        animHandler.sendEmptyMessage(0x00);
    }

    public void stop() {
        if (animHandler != null) {
            animHandler.removeMessages(0x00);
            animHandler = null;
        }
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
        invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidate();
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }
}
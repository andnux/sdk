package top.andnux.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class QuickIndexBar extends View {
    private Paint paint;
    private int mCellWidth;
    private int mCellHeight;
    private float mTextHeight;
    private int currentIndex = -1;
    private int textSize = DensityUtils.sp2px(14);
    private int textColor = Color.parseColor("#2b2b2b");
    private int currentTextColor = Color.parseColor("#2b2b2b");
    private OnLetterChangeListener onLetterChangeListener;
    public void setList(List<String> mList) {
        this.mList = mList;
        mCellHeight = getMeasuredHeight() / mList.size();
        invalidate();
    }

    private List<String> mList;

    public OnLetterChangeListener getOnLetterChangeListener() {
        return onLetterChangeListener;
    }

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener) {
        this.onLetterChangeListener = onLetterChangeListener;
    }

    public interface OnLetterChangeListener {
        void onLetterChange(String letter);

        //手指抬起
        void onReset();
    }


    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexBar);
        textSize = a.getDimensionPixelSize(R.styleable.QuickIndexBar_indexTextSize, textSize);
        textColor = a.getColor(R.styleable.QuickIndexBar_indexTColor, textColor);
        currentTextColor = a.getColor(R.styleable.QuickIndexBar_currentTextColor, currentTextColor);
        a.recycle();
        paint = new Paint();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        //消除锯齿
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        mTextHeight = (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);  //1.1---2   2.1--3
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCellWidth = getMeasuredWidth();
        if (mList == null || mList.isEmpty()){
            return;
        }
        mCellHeight = getMeasuredHeight() / mList.size();
    }

    /**
     * 绘制控件
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        字.画字();
        if (mList == null || mList.isEmpty()){
            return;
        }
        for (int i = 0; i < mList.size(); i++) {
            String letter = mList.get(i);
            float mTextWidth = paint.measureText(letter);
            float x = (mCellWidth - mTextWidth) * 0.5f;
            float y = (mCellHeight + mTextHeight) * 0.5f + mCellHeight * i;

            if (i == currentIndex) {
                paint.setColor(currentTextColor);
            } else {
                paint.setColor(textColor);
            }

            canvas.drawText(letter, x, y, paint);
        }
    }


    /**
     * 处理 按下 移动 手指抬起
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCellHeight == 0){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("我按下了!!");
                int downY = (int) event.getY();
                //获取当前索引
                currentIndex = downY / mCellHeight;
                if (currentIndex < 0 || currentIndex > mList.size() - 1) {

                } else {
//                   ToastUtil.showToast(getContext(),LETTERS[currentIndex]);
                    if (onLetterChangeListener != null) {
                        onLetterChangeListener.onLetterChange(mList.get(currentIndex));
                    }
                }
                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("我移动了!!");
                int moveY = (int) event.getY();
                //获取当前索引
                currentIndex = moveY / mCellHeight;
                if (currentIndex < 0 || currentIndex > mList.size() - 1) {

                } else {
//                    ToastUtil.showToast(getContext(),LETTERS[currentIndex]);
                    if (onLetterChangeListener != null) {
                        onLetterChangeListener.onLetterChange(mList.get(currentIndex));
                    }
                }
                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("我手指抬起了!!");
                currentIndex = -1;
                //手动刷新
                invalidate();
                //表示手指抬起了
                if (onLetterChangeListener != null) {
                    onLetterChangeListener.onReset();
                }
                break;
        }


        // 为了 能够接受  move+up事件
        return true;
    }
}
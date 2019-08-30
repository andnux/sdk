package top.andnux.ui.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import top.andnux.ui.R;
import top.andnux.ui.image.RoundedImageView;

public class RatioImageView extends RoundedImageView {

    private float mRatioWidth;
    private float mRatioHeight;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
        mRatioWidth = a.getFloat(R.styleable.RatioImageView_ratio_width, 1.0f);
        mRatioHeight = a.getFloat(R.styleable.RatioImageView_ratio_height, 1.0f);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float ratio = mRatioWidth / mRatioHeight;
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //宽确定，高不确定
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0) {
            heightSize = (int) (widthSize * ratio + 0.5f);//根据宽度和比例计算高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY & ratio != 0) {
            widthSize = (int) (heightSize / ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        } else {
            throw new RuntimeException("无法设定宽高比");
        }
        //必须调用下面的两个方法之一完成onMeasure方法的重写，否则会报错
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setLayout() {
        int width = getMeasuredWidth();
        int height = (int) (width / mRatioWidth * mRatioHeight);
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        this.setLayoutParams(layoutParams);
    }

    public float getRatioWidth() {
        return mRatioWidth;
    }

    public void setRatioWidth(float ratioWidth) {
        this.mRatioWidth = ratioWidth;
        setLayout();
    }

    public float getRatioHeight() {
        return mRatioHeight;
    }

    public void setRatioHeight(float ratioHeight) {
        this.mRatioHeight = ratioHeight;
        setLayout();
    }
}

package top.andnux.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 两张图片实现开关控件
 */
public class SwitchImageView extends AppCompatImageView implements View.OnClickListener {

    private Drawable mOpenDrawable;
    private Drawable mCloseDrawable;
    private boolean isOpen = false;
    private OnStateListener mStateListener;

    public interface OnStateListener {
        void onStateChange(boolean isOpen);
    }

    public SwitchImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public SwitchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SwitchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SwitchImageView, defStyle, 0);
        mOpenDrawable = a.getDrawable(R.styleable.SwitchImageView_res_open);
        mCloseDrawable = a.getDrawable(R.styleable.SwitchImageView_res_close);
        isOpen = a.getBoolean(R.styleable.SwitchImageView_state, isOpen);
        a.recycle();
        setOpen(isOpen);
        setOnClickListener(this);
    }

    public void setOpen(boolean open) {
        isOpen = open;
        if (isOpen) {
            setImageDrawable(mOpenDrawable);
        } else {
            setImageDrawable(mCloseDrawable);
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public OnStateListener getStateListener() {
        return mStateListener;
    }

    public void setStateListener(OnStateListener stateListener) {
        mStateListener = stateListener;
    }

    @Override
    public void onClick(View v) {
        isOpen = !isOpen;
        setOpen(isOpen);
        if (mStateListener != null) {
            mStateListener.onStateChange(isOpen);
        }
    }
}

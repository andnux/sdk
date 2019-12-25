package top.andnux.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


public class TextViewDrawable extends AppCompatTextView {

    private int drawableLeftWidth;
    private int drawableLeftHeight;
    private int drawableTopWidth;
    private int drawableTopHeight;
    private int drawableRightWidth;
    private int drawableRightHeight;
    private int drawableBottomWidth;
    private int drawableBottomHeight;

    public TextViewDrawable(Context context) {
        super(context);
        init(null, 0);
    }

    public TextViewDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TextViewDrawable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TextViewDrawable, defStyle, 0);
        drawableLeftWidth = a.getDimensionPixelOffset(R.styleable.TextViewDrawable_drawableLeftWidth, drawableLeftWidth);
        drawableLeftHeight = a.getDimensionPixelOffset(R.styleable.TextViewDrawable_drawableLeftHeight, drawableLeftHeight);
        drawableTopWidth = a.getDimensionPixelOffset(R.styleable.TextViewDrawable_drawableTopWidth, drawableTopWidth);
        drawableTopHeight = a.getDimensionPixelOffset(R.styleable.TextViewDrawable_drawableTopHeight, drawableTopHeight);
        drawableRightWidth = a.getDimensionPixelOffset(R.styleable.TextViewDrawable_drawableRightWidth, drawableRightWidth);
        drawableRightHeight = a.getDimensionPixelOffset(R.styleable.TextViewDrawable_drawableRightHeight, drawableRightHeight);
        drawableBottomWidth = a.getDimensionPixelOffset(R.styleable.TextViewDrawable_drawableBottomWidth, drawableBottomWidth);
        drawableBottomHeight = a.getDimensionPixelOffset(R.styleable.TextViewDrawable_drawableBottomHeight, drawableBottomHeight);
        a.recycle();
        Drawable[] drawables = getCompoundDrawables();
        for (int i = 0; i < drawables.length; i++) {
            Drawable drawable = drawables[i];
            if (drawable == null) continue;
            Rect bounds = drawable.getBounds();
            if (i == 0 && drawableLeftWidth != 0 && drawableLeftHeight != 0) {
                drawable.setBounds(bounds.left, bounds.top, drawableLeftWidth, drawableLeftHeight);
            }
            if (i == 1 && drawableTopWidth != 0 && drawableTopHeight != 0) {
                drawable.setBounds(bounds.left, bounds.top, drawableTopWidth, drawableTopHeight);
            }
            if (i == 2 && drawableRightWidth != 0 && drawableRightHeight != 0) {
                drawable.setBounds(bounds.left, bounds.top, drawableRightWidth, drawableRightHeight);
            }
            if (i == 3 && drawableBottomWidth != 0 && drawableBottomHeight != 0) {
                drawable.setBounds(bounds.left, bounds.top, drawableBottomWidth, drawableBottomHeight);
            }
            drawables[i] = drawable;
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}

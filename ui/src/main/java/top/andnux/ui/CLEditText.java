package top.andnux.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CLEditText extends LinearLayout implements TextWatcher {

    private ImageView leftView;
    private EditText mEditText;
    private ImageView rightView;
    private Drawable leftDrawable;
    private Drawable rightDrawable;

    private int textColor = Color.BLACK;
    private float textSize = dip2px(14);
    private String hintText;
    private String text;

    private float leftWidth = dip2px(0);
    private float rightWidth = dip2px(0);

    public CLEditText(Context context) {
        this(context, null);
    }

    public CLEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CLEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.removeAllViews();
        setGravity(Gravity.CENTER_VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CLEditText, defStyle, 0);
        leftDrawable = a.getDrawable(R.styleable.CLEditText_leftDrawable);
        rightDrawable = a.getDrawable(R.styleable.CLEditText_rightDrawable);
        leftWidth = a.getDimension(R.styleable.CLEditText_leftWidth, leftWidth);
        rightWidth = a.getDimension(R.styleable.CLEditText_rightWidth, rightWidth);
        textColor = a.getColor(R.styleable.CLEditText_textColor, textColor);
        textSize = a.getDimension(R.styleable.CLEditText_textSize, textSize);
        hintText = a.getString(R.styleable.CLEditText_hintText);
        text = a.getString(R.styleable.CLEditText_text);
        a.recycle();

        if (leftDrawable != null) {
            leftView = new ImageView(context);
            leftView.setImageDrawable(leftDrawable);
            addView(leftView, new LayoutParams(dip2px(leftWidth), LayoutParams.MATCH_PARENT));
        }

        if (rightDrawable != null) {
            rightView = new ImageView(context);
            rightView.setImageDrawable(rightDrawable);
            addView(rightView, new LayoutParams(dip2px(rightWidth), LayoutParams.MATCH_PARENT));
        }
        mEditText = new EditText(context);
        mEditText.setText(text);
        mEditText.setHint(hintText);
        mEditText.setTextColor(textColor);
        mEditText.setTextSize(textSize);
        mEditText.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
        mEditText.addTextChangedListener(this);
        addView(mEditText, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public String getText() {
        return text;
    }

    public float getTextSize() {
        return textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public String getHintText() {
        return hintText;
    }


    public EditText getEditText() {
        return mEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

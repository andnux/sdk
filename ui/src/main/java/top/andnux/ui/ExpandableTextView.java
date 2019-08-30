package top.andnux.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 可扩展的TextView
 */
public class ExpandableTextView extends AppCompatTextView implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "ExpandableTextView";
    private boolean isExpand;
    private Runnable resumeRunnable;
    private int mExpandTotalLine = -1;
    private int expandTextColor = getTextColors().getDefaultColor();
    private String expandText;
    private Drawable expandIcon;
    private int retractTextColor = getTextColors().getDefaultColor();
    private String retractText;
    private Drawable retractIcon;
    private String mode = "0";

    public ExpandableTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ExpandableTextView, defStyle, 0);
        mode = a.getString(R.styleable.ExpandableTextView_mode);
        mExpandTotalLine = a.getInt(R.styleable.ExpandableTextView_expandTotalLine, mExpandTotalLine);

        expandTextColor = a.getColor(R.styleable.ExpandableTextView_expandTextColor, expandTextColor);
        expandText = a.getString(R.styleable.ExpandableTextView_expandText);
        expandIcon = a.getDrawable(R.styleable.ExpandableTextView_expandIcon);

        retractTextColor = a.getColor(R.styleable.ExpandableTextView_retractTextColor, retractTextColor);
        retractText = a.getString(R.styleable.ExpandableTextView_retractText);
        retractIcon = a.getDrawable(R.styleable.ExpandableTextView_retractIcon);

        a.recycle();
        this.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private class LineContent implements Runnable {

        private TextView mTarget;
        private String mContent;

        LineContent(TextView mTarget, String mContent) {
            this.mTarget = mTarget;
            this.mContent = mContent;
        }

        @Override
        public void run() {
            if (null != mTarget && !TextUtils.isEmpty(mContent)) {
                getLineContent();
            }
        }

        @SuppressLint("SetTextI18n")
        private void setGoodAtText(String textContent, boolean expand) {
            if (mode.equals("0")) { //icon
                SpannableString ss = new SpannableString(textContent);
                Drawable drawable;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable = expand ? expandIcon : retractIcon;
                } else {
                    drawable = expand ? expandIcon : retractIcon;
                }
                drawable.setBounds(0, 0, (int) getTextSize(), (int) getTextSize());
                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                ss.setSpan(imageSpan, textContent.length() - 1, textContent.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                mTarget.setText(ss);
            } else { //text
                if (expand) {
                    SpannableString ss = new SpannableString(textContent + retractText);
                    ForegroundColorSpan span = new ForegroundColorSpan(retractTextColor);
                    ss.setSpan(span, textContent.length(), textContent.length() + retractText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    mTarget.setText(ss);
                } else {
                    SpannableString ss = new SpannableString(textContent + expandText);
                    ForegroundColorSpan span = new ForegroundColorSpan(expandTextColor);
                    ss.setSpan(span, textContent.length(), textContent.length() + expandText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    mTarget.setText(ss);
                }
            }
        }

        private void getLineContent() {
            Layout layout = mTarget.getLayout();
            int lines = mTarget.getLineCount();
            if (isExpand) {
                if (mode.equals("0")) {
                    setGoodAtText(mContent + "\u3000", isExpand);
                } else {
                    setGoodAtText(mContent, isExpand);
                }
            } else {
                if (lines > mExpandTotalLine) {
                    StringBuilder threeLinesContent = new StringBuilder();
                    for (int i = 0; i < mExpandTotalLine; i++) {
                        threeLinesContent.append(mTarget.getText().subSequence(layout.getLineStart(i), layout.getLineEnd(i)).toString());
                    }
                    if (mode.equals("0")) {
                        threeLinesContent = new StringBuilder(threeLinesContent.substring(0, threeLinesContent.length() - 2) + "...\u3000");
                    } else {
                        threeLinesContent = new StringBuilder(threeLinesContent.substring(0, threeLinesContent.length() - expandText.length() - 1) + "...");
                    }
                    setGoodAtText(threeLinesContent.toString(), false);
                    mTarget.setOnClickListener(view -> {
                        Log.d(TAG, "getLineContent() called isExpand = " + isExpand);
                        isExpand = !isExpand;
                        mTarget.post(resumeRunnable);
                    });
                }
            }
        }
    }


    @Override
    public void onGlobalLayout() {
        resumeRunnable = new LineContent(this, getText().toString());
        this.post(resumeRunnable);
        this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
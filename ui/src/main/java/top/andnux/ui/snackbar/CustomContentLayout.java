package top.andnux.ui.snackbar;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author sun on 2018/7/2.
 */
public class CustomContentLayout extends LinearLayout implements  BaseTransientBar.ContentViewCallback {

    public CustomContentLayout(Context context) {
        super(context);
    }

    public CustomContentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomContentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomContentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void animateContentIn(int delay, int duration) {

    }

    @Override
    public void animateContentOut(int delay, int duration) {

    }
}

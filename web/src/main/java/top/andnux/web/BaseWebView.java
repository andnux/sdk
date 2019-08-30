package top.andnux.web;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;

/**
 * 禁止直接写在xml中
 */
public class BaseWebView extends WebView {

    private OnScrollChangedCallback mOnScrollChangedCallback;
    private OnSelectItemListener mOnSelectItemListener;
    private int touchX;
    private int touchY;

    public BaseWebView(Activity context) {
        super(context);
        removeSearchBox();
        setOnLongClickListener(v -> {
            WebView.HitTestResult result = getHitTestResult();
            int type = result.getType();
            String extra = result.getExtra();
            if (mOnSelectItemListener != null && extra != null) {
                boolean isHandle = mOnSelectItemListener.onSelected(touchX, touchY, type, extra);
                if (!isHandle && (type == HitTestResult.IMAGE_TYPE ||
                        type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {
                    WebViewSupport.showSaveImage(context, extra);
                }
            }
            return true;
        });
    }

    public interface OnSelectItemListener {
        boolean onSelected(int x, int y, int type, String extra);
    }

    public OnSelectItemListener getOnSelectItemListener() {
        return mOnSelectItemListener;
    }

    public void setOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        mOnSelectItemListener = onSelectItemListener;
    }

    /**
     * 移除系统注入的对象，避免js漏洞
     */
    private void removeSearchBox() {
        super.removeJavascriptInterface("searchBoxJavaBridge_");
        super.removeJavascriptInterface("accessibility");
        super.removeJavascriptInterface("accessibilityTraversal");
    }

    @Override
    public void setOverScrollMode(int mode) {
        try {
            super.setOverScrollMode(mode);
        } catch (Throwable e) {
            String trace = Log.getStackTraceString(e);
            if (trace.contains("android.content.pm.PackageManager$NameNotFoundException")
                    || trace.contains("java.lang.RuntimeException: Cannot load WebView")
                    || trace.contains("android.webkit.WebViewFactory$MissingWebViewPackageException: Failed to load WebView provider: No WebView installed")) {
                e.printStackTrace();
            } else {
                throw e;
            }
        }
    }

    public void onDestroy() {
        ViewParent parent = this.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this);
        }
        this.stopLoading();
        this.getSettings().setJavaScriptEnabled(false);
        this.clearHistory();
        this.clearView();
        this.removeAllViews();
        this.destroy();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        touchX = (int) event.getRawX();
        touchY = (int) event.getRawY();
        return super.onInterceptTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }
}


package top.andnux.mvvm.adapter;

import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.adapters.ListenerUtil;

import com.bumptech.glide.Glide;

import java.util.Map;

import top.andnux.mvvm.R;

public class BindingProviders {

    @BindingAdapter("url")
    public static void loadUrl(WebView view, String url) {
        view.loadUrl(url);
    }

    @BindingAdapter(value = {"url", "header"}, requireAll = false)
    public static void loadUrl(WebView view, String url, Map<String, String> header) {
        if (header == null || header.isEmpty()) {
            view.loadUrl(url);
        } else {
            view.loadUrl(url, header);
        }
    }

    @BindingAdapter(value = {"data", "mime", "encoding"}, requireAll = false)
    public static void loadData(WebView view, String data, String mime, String encoding) {
        if (TextUtils.isEmpty(mime)) {
            mime = "text/html";
        }
        if (TextUtils.isEmpty(encoding)) {
            mime = "utf-8";
        }
        view.loadData(data, mime, encoding);
    }

    @BindingAdapter("url")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .centerCrop()
                .into(view);
    }

    @BindingAdapter(value = {"url", "error"}, requireAll = false)
    public static void loadImage(ImageView view, String url, Drawable error) {
        Glide.with(view.getContext())
                .load(url)
                .error(error)
                .centerCrop()
                .into(view);
    }


    @BindingAdapter(value = {"url", "error", "placeholder"}, requireAll = false)
    public static void loadImage(ImageView view, String url, Drawable error, Drawable placeHolder) {
        if (url == null) {
            view.setImageDrawable(placeHolder);
        } else {
            Glide.with(view.getContext())
                    .load(url)
                    .error(error)
                    .centerCrop()
                    .into(view);
        }
    }

    @BindingAdapter(value = {"url", "loading", "error", "placeholder"}, requireAll = false)
    public static void loadImage(ImageView view, String url, Drawable loading,
                                 Drawable error, Drawable placeHolder) {
        if (url == null) {
            view.setImageDrawable(placeHolder);
        } else {
            view.setImageDrawable(loading);
            Glide.with(view.getContext())
                    .load(url)
                    .placeholder(placeHolder)
                    .error(error)
                    .centerCrop()
                    .into(view);
        }
    }

    @BindingAdapter("android:paddingLeft")
    public static void setPaddingLeft(View view, int oldPadding, int newPadding) {
        if (oldPadding != newPadding) {
            view.setPadding(newPadding,
                    view.getPaddingTop(),
                    view.getPaddingRight(),
                    view.getPaddingBottom());
        }
    }

    @BindingAdapter("android:paddingTop")
    public static void setPaddingTop(View view, int oldPadding, int newPadding) {
        if (oldPadding != newPadding) {
            view.setPadding(view.getPaddingLeft(),
                    newPadding,
                    view.getPaddingRight(),
                    view.getPaddingBottom());
        }
    }

    @BindingAdapter("android:paddingRight")
    public static void setPaddingRight(View view, int oldPadding, int newPadding) {
        if (oldPadding != newPadding) {
            view.setPadding(view.getPaddingLeft(),
                    view.getPaddingTop(),
                    newPadding,
                    view.getPaddingBottom());
        }
    }

    @BindingAdapter("android:paddingBottom")
    public static void setPaddingBottom(View view, int oldPadding, int newPadding) {
        if (oldPadding != newPadding) {
            view.setPadding(view.getPaddingLeft(),
                    view.getPaddingTop(),
                    view.getPaddingRight(),
                    newPadding);
        }
    }

    @BindingAdapter("android:onLayoutChange")
    public static void setOnLayoutChangeListener(View view, View.OnLayoutChangeListener oldValue,
                                                 View.OnLayoutChangeListener newValue) {
        if (oldValue != null) {
            view.removeOnLayoutChangeListener(oldValue);
        }
        if (newValue != null) {
            view.addOnLayoutChangeListener(newValue);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public interface OnViewDetachedFromWindow {
        void onViewDetachedFromWindow(View v);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public interface OnViewAttachedToWindow {
        void onViewAttachedToWindow(View v);
    }

    @BindingAdapter(value = {"android:onViewDetachedFromWindow",
            "android:onViewAttachedToWindow"}, requireAll = false)
    public static void setListener(View view, final OnViewDetachedFromWindow detach,
                                   final OnViewAttachedToWindow attach) {
        View.OnAttachStateChangeListener newListener;
        if (detach == null && attach == null) {
            newListener = null;
        } else {
            newListener = new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    if (attach != null) {
                        attach.onViewAttachedToWindow(v);
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    if (detach != null) {
                        detach.onViewDetachedFromWindow(v);
                    }
                }
            };
        }

        View.OnAttachStateChangeListener oldListener = ListenerUtil.trackListener(view, newListener,
                R.id.onAttachStateChangeListener);
        if (oldListener != null) {
            view.removeOnAttachStateChangeListener(oldListener);
        }
        if (newListener != null) {
            view.addOnAttachStateChangeListener(newListener);
        }
    }

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}

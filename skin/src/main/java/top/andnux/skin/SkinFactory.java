package top.andnux.skin;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.LayoutInflaterFactory;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

import top.andnux.skin.attr.SkinAttr;
import top.andnux.skin.attr.SkinView;
import top.andnux.skin.callback.ISkinChangeListener;
import top.andnux.skin.support.SkinAppCompatViewInflater;
import top.andnux.skin.support.SkinAttrSupport;

public class SkinFactory implements LayoutInflaterFactory {

    // 后面会写插件换肤 预留的东西
    private SkinAppCompatViewInflater mAppCompatViewInflater;
    private Activity mContext;
    private ISkinChangeListener mSkinChangeListener;


    public SkinFactory(Activity context, ISkinChangeListener listener) {
        mContext = context;
        mSkinChangeListener = listener;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LayoutInflaterCompat.setFactory(layoutInflater, this);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 拦截到View的创建  获取View之后要去解析
        // 1. 创建View
        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);
        // 2. 解析属性  src  textColor  background  自定义属性
        // Log.e(TAG, view + "");
        // 2.1 一个activity的布局肯定对应多个这样的 SkinView
        if (view != null) {
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view, skinAttrs);
            // 3.统一交给SkinManager管理
            managerSkinView(skinView);

            // 4.判断一下要不要换肤
            SkinManager.getInstance().checkChangeSkin(skinView);
        }
        return view;
    }

    /**
     * 统一管理SkinView
     */
    private void managerSkinView(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(mContext);
        if (skinViews == null) {
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(mSkinChangeListener, skinViews);
        }
        skinViews.add(skinView);
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }
        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);
        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            return false;
        }
        while (true) {
            if (parent == null) {
                return true;
            } else if (parent == mContext.getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                return false;
            }
            parent = parent.getParent();
        }
    }

    public void onDestroy() {
        SkinManager.getInstance().unregister(mSkinChangeListener);
    }
}

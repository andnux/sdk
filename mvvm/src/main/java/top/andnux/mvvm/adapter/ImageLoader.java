package top.andnux.mvvm.adapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public interface ImageLoader {

    void display(ImageView view,
                 String url,
                 Drawable loading,
                 Drawable placeholder,
                 Drawable error);
}

package top.andnux.adapter;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * More convenient chain call.
 * <p>
 * Created by andnux on 2017/3/29.
 */
public class ExtendViewHolder {
    private View currentView;

    private ExtendViewHolder(View view) {
        currentView = view;
    }

    static top.andnux.adapter.ExtendViewHolder get(View view) {
        return new top.andnux.adapter.ExtendViewHolder(view);
    }

    public top.andnux.adapter.ExtendViewHolder setText(CharSequence text) {
        ((TextView) currentView).setText(text);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setTextColor(int textColor) {
        ((TextView) currentView).setTextColor(textColor);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setTextColor(ColorStateList colorStateList) {
        ((TextView) currentView).setTextColor(colorStateList);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setMovementMethod(MovementMethod method) {
        ((TextView) currentView).setMovementMethod(method);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setImageResource(@DrawableRes int resId) {
        ((ImageView) currentView).setImageResource(resId);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setImageDrawable(Drawable drawable) {
        ((ImageView) currentView).setImageDrawable(drawable);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setImageBitmap(Bitmap bitmap) {
        ((ImageView) currentView).setImageBitmap(bitmap);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setImageUri(Uri imageUri) {
        ((ImageView) currentView).setImageURI(imageUri);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setScaleType(ImageView.ScaleType type) {
        ((ImageView) currentView).setScaleType(type);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setBackgroundColor(@ColorInt int bgColor) {
        currentView.setBackgroundColor(bgColor);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setBackgroundResource(@DrawableRes int bgRes) {
        currentView.setBackgroundResource(bgRes);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setColorFilter(ColorFilter colorFilter) {
        ((ImageView) currentView).setColorFilter(colorFilter);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setColorFilter(int colorFilter) {
        ((ImageView) currentView).setColorFilter(colorFilter);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setAlpha(@FloatRange(from = 0.0, to = 1.0) float value) {
        ViewCompat.setAlpha(currentView, value);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setVisibility(int visibility) {
        currentView.setVisibility(visibility);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setMax(int max) {
        ((ProgressBar) currentView).setMax(max);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setProgress(int progress) {
        ((ProgressBar) currentView).setProgress(progress);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setRating(float rating) {
        ((RatingBar) currentView).setRating(rating);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setTag(Object tag) {
        currentView.setTag(tag);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setEnabled(boolean enabled) {
        currentView.setEnabled(enabled);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setAdapter(Adapter adapter) {
        ((AdapterView<Adapter>) currentView).setAdapter(adapter);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setAdapter(RecyclerView.Adapter adapter) {
        ((RecyclerView) currentView).setAdapter(adapter);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setChecked(boolean checked) {
        ((Checkable) currentView).setChecked(checked);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setOnClickListener(View.OnClickListener listener) {
        currentView.setOnClickListener(listener);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setOnLongClickListener(View.OnLongClickListener listener) {
        currentView.setOnLongClickListener(listener);
        return this;
    }

    public top.andnux.adapter.ExtendViewHolder setOnTouchListener(View.OnTouchListener listener) {
        currentView.setOnTouchListener(listener);
        return this;
    }

}

package top.andnux.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import top.andnux.skin.SkinManager;
import top.andnux.skin.SkinResource;

public enum SkinType {
    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorByName(resName);
            if (color == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setTextColor(color);
        }
    }, TEXT_COLOR_HINT("textColorHint") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorByName(resName);
            if (color == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setHintTextColor(color);
        }
    },
    BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            // 背景可能是图片也可能是颜色
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if(drawable!=null){
                ImageView imageView = (ImageView) view;
                imageView.setBackgroundDrawable(drawable);
                return;
            }
            // 可能是颜色
            ColorStateList color = skinResource.getColorByName(resName);
            if(color!=null){
                view.setBackgroundColor(color.getDefaultColor());
            }
        }
    },SRC("src") {
        @Override
        public void skin(View view, String resName) {
            // 获取资源设置
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if(drawable!=null){
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
            }
        }
    }, DRAWABLE_LEFT("drawableLeft") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                TextView textView = (TextView) view;
                Drawable[] drawables = textView.getCompoundDrawables();
                textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }
    }, DRAWABLE_RIGHT("drawableRight") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                TextView textView = (TextView) view;
                Drawable[] drawables = textView.getCompoundDrawables();
                textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }
    }, DRAWABLE_TOP("drawableTop") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                TextView textView = (TextView) view;
                Drawable[] drawables = textView.getCompoundDrawables();
                textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }
    }, DRAWABLE_START("drawableStart") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                TextView textView = (TextView) view;
                Drawable[] drawables = textView.getCompoundDrawables();
                textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }
    }, DRAWABLE_END("drawableEnd") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                TextView textView = (TextView) view;
                Drawable[] drawables = textView.getCompoundDrawables();
                textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }
    }, DRAWABLE_BOTTOM("drawableBottom") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                TextView textView = (TextView) view;
                Drawable[] drawables = textView.getCompoundDrawables();
                textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }
    }, BACKGROUND_TINT("backgroundTint") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList tint = skinResource.getColorByName(resName);
            if (tint == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setBackgroundTintList(tint);
        }
    }, DRAWABLE_TINT("drawableTint") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList tint = skinResource.getColorByName(resName);
            if (tint == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setBackgroundTintList(tint);
        }
    }, FOREGROUND_TINT("foregroundTint") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList tint = skinResource.getColorByName(resName);
            if (tint == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setBackgroundTintList(tint);
        }
    }, BUTTON("button") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable == null) {
                return;
            }
            CheckBox checkBox = (CheckBox) view;
            checkBox.setButtonDrawable(drawable);
        }
    }, BUTTON_TINT("buttonTint") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList tint = skinResource.getColorByName(resName);
            if (tint == null) {
                return;
            }
            CheckBox checkBox = (CheckBox) view;
            checkBox.setButtonTintList(tint);
        }
    }, THUMB("thumb") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable == null) {
                return;
            }
            SeekBar seekBar = (SeekBar) view;
            seekBar.setThumb(drawable);
        }
    }, THUMB_TINT("thumbTint") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList tint = skinResource.getColorByName(resName);
            if (tint == null) {
                return;
            }
            SeekBar seekBar = (SeekBar) view;
            seekBar.setThumbTintList(tint);
        }
    };

    // 会根据名字调对应的方法
    private String mResName;
    SkinType(String resName){
        this.mResName = resName;
    }

    // 抽象的
    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }

    public SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }
}

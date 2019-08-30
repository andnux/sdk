package top.andnux.mvp.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import top.andnux.base.annotation.ContentView;

public abstract class BaseActivity<V extends BaseView, M extends BaseModel,
        P extends BasePresenter<V, M>> extends AppCompatActivity
        implements BaseView {

    protected final String TAG = this.getClass().getSimpleName();
    protected P presenter;

    protected Object getContentView() {
        return null;
    }
    @SuppressWarnings("all")
    protected P createPresenter() {
        try {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) type;
                Type clazz = pType.getActualTypeArguments()[2];
                if (clazz instanceof Class) {
                    Constructor constructor = ((Class) clazz).getConstructor(Context.class);
                    return (P) constructor.newInstance(this);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("all")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        presenter = createPresenter();
        presenter.attachView((V) this);
    }

    private void initContentView() {
        Object contentView = getContentView();
        if (contentView == null) {
            ContentView annotation = this.getClass().getAnnotation(ContentView.class);
            if (annotation != null && annotation.value() != 0) {
                setContentView(annotation.value());
            }
        } else {
            if (contentView instanceof View) {
                setContentView((View) contentView);
            } else if (contentView instanceof Integer) {
                setContentView((Integer) contentView);
            }
        }
    }


    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}

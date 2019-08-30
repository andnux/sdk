package top.andnux.mvp.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import top.andnux.base.annotation.ContentView;

public abstract class BaseFragment<V extends BaseView, M extends BaseModel,
        P extends BasePresenter<V, M>> extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Object contentView = getContentView();
        if (contentView == null) {
            ContentView annotation = this.getClass().getAnnotation(ContentView.class);
            if (annotation != null && annotation.value() != 0) {
                return inflater.inflate(annotation.value(), container, false);
            }
        } else {
            if (contentView instanceof View) {
                return (View) contentView;
            } else if (contentView instanceof Integer) {
                return inflater.inflate((Integer) (contentView), container, false);
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = createPresenter();
        presenter.attachView((V) this);
    }

    @Override
    public void onDetach() {
        presenter.detachView();
        super.onDetach();
    }
}

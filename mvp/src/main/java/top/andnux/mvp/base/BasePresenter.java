package top.andnux.mvp.base;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import top.andnux.base.annotation.Provider;

public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {

    protected V mView;
    protected M mModel;
    private WeakReference<Context> mContext;

    public BasePresenter(Context context) {
        mContext = new WeakReference<>(context);
    }

    @SuppressWarnings("all")
    protected M createModel() {
        try {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) type;
                Type clazz = pType.getActualTypeArguments()[1];
                if (clazz instanceof Class) {
                    try {
                        Provider annotation = (Provider)
                                ((Class) clazz).getAnnotation(Provider.class);
                        Class<?> value = annotation.value();
                        return (M) value.newInstance();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    void attachView(V v) {
        mView = v;
        mModel = createModel();
    }

    protected Context getActivity() {
        return mContext.get();
    }

    /**
     * 解除绑定V层
     */
    void detachView() {
        mView = null;
        mModel = null;
        mContext.clear();
        mContext = null;
    }
}

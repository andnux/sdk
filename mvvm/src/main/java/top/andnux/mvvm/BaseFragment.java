package top.andnux.mvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import top.andnux.base.annotation.ContentView;

public abstract class BaseFragment<T extends ViewDataBinding, VM extends ViewModel> extends Fragment {

    protected T mBinding;
    protected VM mModel;

    protected abstract void onCreated(@Nullable Bundle savedInstanceState);

    protected int getVariableId() {
        return -1;
    }

    @SuppressWarnings("all")
    protected VM createViewModel() {
        try {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) type;
                Type clazz = pType.getActualTypeArguments()[1];
                if (clazz instanceof Class) {
                    return (VM) ((Class) clazz).newInstance();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object getContentView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return initContentView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = DataBindingUtil.bind(view);
        mModel = createViewModel();
        mBinding.setLifecycleOwner(this);
        if (getVariableId() == -1) {
            try {
                Method setViewModel = mBinding.getClass().getMethod("setViewModel",
                        mModel.getClass());
                setViewModel.invoke(mBinding, mModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mBinding.setVariable(getVariableId(), mModel);
        }
        onCreated(savedInstanceState);
    }

    private View initContentView() {
        Object contentView = getContentView();
        View view = null;
        if (contentView == null) {
            ContentView annotation = this.getClass().getAnnotation(ContentView.class);
            if (annotation != null && annotation.value() != 0) {
                view = LayoutInflater.from(getContext()).inflate(annotation.value(), null);
            }
        } else {
            if (contentView instanceof View) {
                view = (View) contentView;
            } else if (contentView instanceof Integer) {
                view = LayoutInflater.from(getContext()).inflate((Integer) contentView, null);
            }
        }
        return view;
    }
}

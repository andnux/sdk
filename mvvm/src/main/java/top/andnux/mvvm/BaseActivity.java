package top.andnux.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import top.andnux.base.annotation.ContentView;

public abstract class BaseActivity<T extends ViewDataBinding, VM extends ViewModel> extends AppCompatActivity {

    protected T mBinding;
    protected VM mModel;

    protected abstract void onCreated(@Nullable Bundle savedInstanceState);

    protected int getVariableId() {
        return -1;
    }

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
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

    private void initContentView() {
        Object contentView = getContentView();
        if (contentView == null) {
            ContentView annotation = this.getClass().getAnnotation(ContentView.class);
            if (annotation != null && annotation.value() != 0) {
                mBinding = DataBindingUtil.setContentView(this, annotation.value());
            }
        } else {
            if (contentView instanceof View) {
                setContentView((View) contentView);
                mBinding = DataBindingUtil.bind((View) contentView);
            } else if (contentView instanceof Integer) {
                mBinding = DataBindingUtil.setContentView(this, (Integer) contentView);
            }
        }
    }

}

package top.andnux.skin;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterFactory;

import top.andnux.skin.callback.ISkinChangeListener;

public abstract class BaseSkinActivity extends AppCompatActivity
        implements LayoutInflaterFactory, ISkinChangeListener {

    private SkinFactory mFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mFactory = new SkinFactory(this, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        mFactory.onDestroy();
        super.onDestroy();
    }
}

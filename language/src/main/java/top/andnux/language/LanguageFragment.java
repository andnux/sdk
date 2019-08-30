package top.andnux.language;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class LanguageFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private LanguageAdapter mAdapter;
    private List<LanguageBean> mBeans = new ArrayList<>();

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        mListView = new ListView(getContext());
        return mListView;
    }

    public void setLanguageAdapter(LanguageAdapter adapter) {
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
        LanguageBean locale = LocalManageUtil.getSetLanguageLocale(getContext());
        for (int i = 0; i < mBeans.size(); i++) {
            LanguageBean bean = mBeans.get(i);
            if (locale.getCode().equalsIgnoreCase(bean.getCode())) {
                bean.setSelect(true);
            } else {
                bean.setSelect(false);
            }
            mBeans.set(i, bean);
        }
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLanguage(mBeans);
    }

    protected void initLanguage(List<LanguageBean> list) {
        list.add(new LanguageBean("auto", "跟随系统", LocalManageUtil.getSystemLocale(getContext())));
        list.add(new LanguageBean("zh_cn", "简体中文", Locale.SIMPLIFIED_CHINESE));
        list.add(new LanguageBean("en", "english", Locale.ENGLISH));
    }
}

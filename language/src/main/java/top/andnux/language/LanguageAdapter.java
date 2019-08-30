package top.andnux.language;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class LanguageAdapter extends BaseAdapter {

    private Context mContext;
    private List<LanguageBean> mBeans;


    @Override
    public int getCount() {
        return mBeans == null ? 0 : mBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

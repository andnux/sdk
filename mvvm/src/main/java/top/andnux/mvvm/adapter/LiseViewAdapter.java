package top.andnux.mvvm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.List;

/**
 * 会有问题，还没解决
 * 建议使用 RecyclerViewAdapter
 *
 * @param <T>
 */
@Deprecated
public class LiseViewAdapter<T> extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int layoutId;
    private int variableId;
    private List<T> mList;

    public LiseViewAdapter(Context context, int layoutId,
                           int variableId, List<T> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.variableId = variableId;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewDataBinding binding = null;
        if (view == null) {
            binding = DataBindingUtil.inflate(mLayoutInflater, layoutId, viewGroup, false);
        } else {
            binding = DataBindingUtil.getBinding(view);
        }
        if (binding != null) {
            binding.setVariable(variableId, mList.get(i));
        }
        if (binding != null) {
            return binding.getRoot().getRootView();
        }
        return view;
    }
}

package top.andnux.mvvm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int layoutId;
    private int variableId;
    private List<T> mList;

    public RecyclerViewAdapter(Context context, int layoutId,
                               int variableId, List<T> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.variableId = variableId;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding inflate = DataBindingUtil.inflate(mLayoutInflater,
                layoutId, parent, false);
        return new ViewHolder(inflate.getRoot().getRootView());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (binding != null) {
            binding.setVariable(variableId, mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

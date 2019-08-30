package top.andnux.ui.pagergridview;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.andnux.ui.DensityUtils;
import top.andnux.ui.R;
import top.andnux.ui.image.RoundedImageView;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private Context mContext;
    private List<GridDataBean> mDataList;
    private OnItemClickListener mListener;
    private ItemAttrs mAttrs;


    public GridViewAdapter(Context context, List<GridDataBean> dataList, ItemAttrs attrs) {
        mContext = context;
        mDataList = dataList;
        mAttrs = attrs;
    }

    public OnItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout itemView = new RelativeLayout(mContext);
        RoundedImageView imageView = new RoundedImageView(mContext);
        LinearLayout content = new LinearLayout(mContext);
        content.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                mAttrs.iconWidth, mAttrs.iconHeight);
        content.setGravity(Gravity.CENTER);
        imageView.setId(R.id.icon);
        imageView.setCornerRadius(mAttrs.iconBorderRadius);
        imageView.setBorderColor(mAttrs.iconBorderColor);
        imageView.setBorderWidth(Float.valueOf(mAttrs.iconBorderWidth));
        content.addView(imageView, iconParams);

        TextView textView = new TextView(mContext);
        textView.setId(R.id.text);
        textView.setTextSize(DensityUtils.px2sp(mAttrs.textSize));
        textView.setTextColor(mAttrs.textColor);
        textView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.topMargin = mAttrs.iconTextMargin;
        content.addView(textView, textParams);

        RelativeLayout.LayoutParams contentLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.getWidth() / mAttrs.columnCount);
        contentLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        itemView.addView(content, contentLayout);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(params);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(holder.itemView, position);
            }
        });
        GridDataBean dataBean = mDataList.get(position);
//        holder.tv_text.setText(dataBean.title);
        holder.tv_text.setText(String.valueOf(position));
        holder.iv_img.setImageResource(R.drawable.ic_android_black_24dp);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_img;
        private TextView tv_text;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.icon);
            tv_text = itemView.findViewById(R.id.text);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}
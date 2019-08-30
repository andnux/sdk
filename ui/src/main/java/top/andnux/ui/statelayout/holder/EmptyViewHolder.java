package top.andnux.ui.statelayout.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import top.andnux.ui.R;


public class EmptyViewHolder extends BaseHolder {

    public ImageView ivImg;

    public EmptyViewHolder(View view) {
        tvTip = (TextView) view.findViewById(R.id.tv_message);
        ivImg = (ImageView) view.findViewById(R.id.iv_img);
    }


}

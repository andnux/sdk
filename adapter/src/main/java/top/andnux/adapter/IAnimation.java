package top.andnux.adapter;


import androidx.recyclerview.widget.RecyclerView;

import top.andnux.adapter.animation.BaseAnimation;

/**
 * Animation interface for adapter.
 * <p>
 * Created by andnux on 16/6/28.
 */
interface IAnimation {

    void enableLoadAnimation();

    void enableLoadAnimation(long duration, BaseAnimation animation);

    void cancelLoadAnimation();

    void setOnlyOnce(boolean onlyOnce);

    void addLoadAnimation(RecyclerView.ViewHolder holder);
}

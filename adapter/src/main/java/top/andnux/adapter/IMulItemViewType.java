package top.andnux.adapter;


import androidx.annotation.LayoutRes;

/**
 * Interface for multiple view types.
 * <p>
 * Created by andnux on 15/11/28.
 */
public interface IMulItemViewType<T> {

    /**
     * @return Will not be called if using a RecyclerView.
     */
    int getViewTypeCount();

    /**
     * Item view type, a non-negative integer is better.
     *
     * @param position current position of ViewHolder
     * @param t        model item
     * @return viewType
     */
    int getItemViewType(int position, T t);

    /**
     * Layout res.
     *
     * @param viewType {@link #getItemViewType(int, T)}
     * @return {@link androidx.annotation.LayoutRes}
     */
    @LayoutRes
    int getLayoutId(int viewType);
}
package top.andnux.adapter;

/**
 * Convenient class for RecyclerView.Adapter.
 * <p>
 * Created by andnux on 16/4/5.
 */
public abstract class SimpleMulItemViewType<T> implements IMulItemViewType<T> {

    @Override
    public int getViewTypeCount() {
        return 1;
    }

}

package top.andnux.adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Methods about layout manager.
 * <p>
 * Created by andnux on 16/1/18.
 */
interface ILayoutManager {

    boolean hasLayoutManager();

    RecyclerView.LayoutManager getLayoutManager();
}

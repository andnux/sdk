package top.andnux.adapter;

import android.view.View;

/**
 * OnItemLongClickListener for RecyclerView.
 * <p>
 * Created by andnux on 16/2/24.
 */
public interface OnItemLongClickListener {

    void onItemLongClick(View itemView, int viewType, int position);

}

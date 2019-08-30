package top.andnux.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EqualGridLayoutManager extends GridLayoutManager {

    public EqualGridLayoutManager(Context context, AttributeSet attrs,
                                  int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public EqualGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public EqualGridLayoutManager(Context context, int spanCount, int orientation,
                                  boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec,
                          int heightSpec) {
        try {
            View view = recycler.getViewForPosition(0);
            measureChild(view, widthSpec, heightSpec);
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
//                int measuredHeight = view.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredWidth);
        }catch (Exception e){
            e.printStackTrace();
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }
}

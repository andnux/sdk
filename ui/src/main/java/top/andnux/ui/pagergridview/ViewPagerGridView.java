package top.andnux.ui.pagergridview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import top.andnux.ui.DensityUtils;
import top.andnux.ui.R;

public class ViewPagerGridView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private LinearLayout mDotLayout;
    private int iconBorderWidth = 0;
    private int iconBorderColor = 0;
    private int iconBorderRadius = 0;
    private int dotMarginBottom = 0;
    private int dotHeight = DensityUtils.dip2px(10);
    private int dotWidth = DensityUtils.dip2px(10);
    private int dotGravity = 1;
    private int dotMargin = DensityUtils.dip2px(6);
    private int rowCount = 3;
    private int columnCount = 4;
    private int iconWidth = DensityUtils.dip2px(50);
    private int iconHetght = DensityUtils.dip2px(50);
    private int textSize = DensityUtils.px2sp(16);
    private int textColor = Color.BLACK;
    private int dotSelectColor = Color.RED;
    private int dotNormalColor = Color.YELLOW;
    private GridViewAdapter.OnItemClickListener mListener;
    private int iconTextMargin;

    public ViewPagerGridView(Context context) {
        super(context);
        init(context, null);
    }

    public ViewPagerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ViewPagerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ViewPagerGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int childCount = getChildCount();
//        int heigth  = 0;
//        for (int i = 0; i < columnCount; i++) {
//            View view = getChildAt(i);
//            if (view == null) return;
//            view.measure(widthMeasureSpec,heightMeasureSpec);
//            heigth = Math.max(view.getMeasuredHeight(),heigth);
//        }
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heigth,MeasureSpec.AT_MOST);
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context, AttributeSet attrs) {
        mViewPager = new ViewPager(context);
        RelativeLayout.LayoutParams viewPagerParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        addView(mViewPager, viewPagerParams);
        mDotLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams dotLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dotLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerGridView);
            dotGravity = a.getInt(R.styleable.ViewPagerGridView_dot_gravity, 1);
            dotMarginBottom = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_dot_margin_bottom, 0);
            dotHeight = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_dot_height, dotHeight);
            dotWidth = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_dot_width, dotWidth);
            dotMargin = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_dot_margin, dotMargin);
            rowCount = a.getInteger(R.styleable.ViewPagerGridView_row_count, rowCount);
            columnCount = a.getInteger(R.styleable.ViewPagerGridView_column_count, columnCount);
            iconBorderWidth = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_icon_border_width, iconBorderWidth);
            iconBorderRadius = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_icon_corner_radius, iconBorderRadius);
            iconBorderColor = a.getColor(R.styleable.ViewPagerGridView_icon_border_color, iconBorderColor);
            iconWidth = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_icon_width, iconWidth);
            iconHetght = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_icon_height, iconHetght);
            iconTextMargin = a.getDimensionPixelOffset(R.styleable.ViewPagerGridView_icon_text_margin, iconTextMargin);
            textSize = a.getDimensionPixelSize(R.styleable.ViewPagerGridView_text_size, textSize);
            textColor = a.getColor(R.styleable.ViewPagerGridView_text_color, textColor);
            dotSelectColor = a.getColor(R.styleable.ViewPagerGridView_dot_select_color, dotSelectColor);
            dotNormalColor = a.getColor(R.styleable.ViewPagerGridView_dot_normal_color, dotNormalColor);
            if (dotGravity == 0) {
                dotLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            } else if (dotGravity == 1) {
                dotLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            } else if (dotGravity == 2) {
                dotLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            } else {
                dotLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            }
            dotLayoutParams.bottomMargin = dotMarginBottom;
            a.recycle();
        }
        addView(mDotLayout, dotLayoutParams);
    }

    public void setBeans(List<GridDataBean> beans) {
        int pageCountMax = columnCount * rowCount;
        int surplus = beans.size() % pageCountMax;
        int pageSize = beans.size() / pageCountMax + (surplus == 0 ? 0 : 1);
        ArrayList<RecyclerView> gridList = new ArrayList<>();
        ItemAttrs attrs = new ItemAttrs();
        attrs.iconHeight = iconHetght;
        attrs.iconWidth = iconWidth;
        attrs.textSize = textSize;
        attrs.textColor = textColor;
        attrs.columnCount = columnCount;
        attrs.iconBorderColor = iconBorderColor;
        attrs.iconBorderRadius = iconBorderRadius;
        attrs.iconBorderWidth = iconBorderWidth;
        attrs.iconTextMargin = iconTextMargin;
        for (int i = 0; i < pageSize; i++) {
            RecyclerView view = new RecyclerView(getContext());
            view.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
            List<GridDataBean> childData = new ArrayList<>();
            int start = i * pageCountMax;
            int end = Math.min(start + pageCountMax, beans.size());
            for (int y = start; y < end; y++) {
                childData.add(beans.get(y));
            }
            GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), childData, attrs);
            gridViewAdapter.setListener(mListener);
            view.setAdapter(gridViewAdapter);
            gridList.add(view);
        }
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(gridList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        //创建视图
        for (int i = 0; i < pageSize; i++) {
            //添加点
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.ic_dot);
            if (i == 0) {
                imageView.setImageTintList(ColorStateList.valueOf(dotSelectColor));
            } else {
                imageView.setImageTintList(ColorStateList.valueOf(dotNormalColor));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotWidth, dotHeight);
            params.leftMargin = dotMargin / 2;
            params.rightMargin = dotMargin / 2;
            mDotLayout.addView(imageView, params);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int childCount = mDotLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) mDotLayout.getChildAt(i);
            if (i == position) {
                imageView.setImageTintList(ColorStateList.valueOf(dotSelectColor));
            } else {
                imageView.setImageTintList(ColorStateList.valueOf(dotNormalColor));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

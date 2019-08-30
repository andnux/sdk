package top.andnux.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.andnux.ui.R;


public class BottomSheetDialog {

    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private boolean showTitle = false;
    private RecyclerView mRecyclerView;
    private List<SheetItem> sheetItemList;
    private Display display;
    private boolean showCancel = true;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    public BottomSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            display = windowManager.getDefaultDisplay();
        }
        builder();
    }

    private void builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_bottom_sheet, null);
        view.setMinimumWidth(display.getWidth());
        txt_title = view.findViewById(R.id.txt_title);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(v -> dialog.dismiss());
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        }
        WindowManager.LayoutParams lp = null;
        if (dialogWindow != null) {
            lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
        }
    }

    public BottomSheetDialog setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        return this;
    }

    public BottomSheetDialog setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    public BottomSheetDialog setCancel(String title) {
        showCancel = true;
        txt_cancel.setVisibility(View.VISIBLE);
        txt_cancel.setText(title);
        return this;
    }

    public BottomSheetDialog setShowCancel(boolean showCancel) {
        this.showCancel = showCancel;
        txt_cancel.setVisibility(showCancel ? View.VISIBLE : View.GONE);
        return this;
    }

    public BottomSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public BottomSheetDialog setCancelListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
        return this;
    }

    public BottomSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public BottomSheetDialog addSheetItem(String strItem, SheetItemColor color,
                                          OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(strItem, color, listener));
        return this;
    }

    private void setSheetItems() {
        int size = sheetItemList.size();
        if (showCancel) {
            txt_cancel.setVisibility(View.VISIBLE);
        } else {
            txt_cancel.setVisibility(View.GONE);
        }
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(layoutManager);
        }
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == RecyclerView.VERTICAL) {
                if (size >= 7) {
                    LayoutParams params = (LayoutParams) mRecyclerView
                            .getLayoutParams();
                    params.height = display.getHeight() / 2;
                    mRecyclerView.setLayoutParams(params);
                }
            }
        }
        if (mAdapter == null) {
            mAdapter = new ListRecyclerAdapter(context, sheetItemList, showTitle, dialog);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        SheetItem(String name, SheetItemColor color,
                  OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    public enum SheetItemColor {
        Blue("#037BFF"), Red("#FD4A2E");

        private String name;

        SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

        private Context mContext;
        private List<SheetItem> mItems;
        private boolean showTitle;
        private Dialog mDialog;

        ListRecyclerAdapter(Context context, List<SheetItem> items, boolean showTitle, Dialog dialog) {
            mContext = context;
            mItems = items;
            this.showTitle = showTitle;
            mDialog = dialog;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(new TextView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SheetItem sheetItem = mItems.get(position);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            if (showTitle) {
                if (position == (mItems.size() - 1)) {
                    holder.mTextView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    holder.mTextView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                }
            } else {
                if (position == 0) {
                    holder.mTextView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                } else if (position == (mItems.size() - 1)) {
                    holder.mTextView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    holder.mTextView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                }
            }
            if (color == null) {
                holder.mTextView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                holder.mTextView.setTextColor(Color.parseColor(color.getName()));
            }

            final OnSheetItemClickListener listener = sheetItem.itemClickListener;

            holder.mTextView.setText(strItem);
            holder.mTextView.setTextSize(18);
            holder.mTextView.setGravity(Gravity.CENTER);
            float scale = mContext.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            holder.mTextView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, height));
            holder.mTextView.setOnClickListener(v -> {
                listener.onClick(position);
                mDialog.dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView mTextView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTextView = (TextView) itemView;
            }
        }
    }
}

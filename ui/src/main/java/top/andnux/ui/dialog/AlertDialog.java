package top.andnux.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import top.andnux.ui.R;

public class AlertDialog {

    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private Button btn_neg;
    private Button btn_pos;
    private Button btn_center;
    private ImageView img_line1;
    private ImageView img_line2;
    private Display display;
    private boolean showTitle = false;
    private boolean showMessage = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean showCenterBtn = false;

    public AlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        builder();
    }

    private void builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialog, null);
        lLayout_bg = view.findViewById(R.id.lLayout_bg);
        txt_title = view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_neg = view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        btn_center = view.findViewById(R.id.btn_center);
        btn_center.setVisibility(View.GONE);
        img_line1 = view.findViewById(R.id.img_line1);
        img_line1.setVisibility(View.GONE);
        img_line2 = view.findViewById(R.id.img_line2);
        img_line2.setVisibility(View.GONE);
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
    }

    public AlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("温馨提示");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    @Deprecated
    public AlertDialog setMsg(String msg) {
        showMessage = true;
        txt_msg.setText(msg);
        return this;
    }

    public AlertDialog setMessage(String msg) {
        showMessage = true;
        txt_msg.setText(msg);
        return this;
    }

    public AlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialog setCancelListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
        return this;
    }

    public AlertDialog setPositiveButton(String text,
                                         final OnClickListener listener) {
        showPosBtn = true;
        btn_pos.setText(text);
        btn_pos.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v);
            }
            dialog.dismiss();
        });
        return this;
    }

    public AlertDialog setCenterButton(String text,
                                       final OnClickListener listener) {
        showCenterBtn = true;
        btn_center.setText(text);
        btn_center.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v);
            }
            dialog.dismiss();
        });
        return this;
    }

    public AlertDialog setNegativeButton(String text,
                                         final OnClickListener listener) {
        showNegBtn = true;
        btn_neg.setText(text);
        btn_neg.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v);
            }
            dialog.dismiss();
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMessage) {
            txt_title.setText("⚠️警告");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMessage) {
            txt_msg.setVisibility(View.VISIBLE);
        }
        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("取消");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(v -> dialog.dismiss());
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line1.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && showNegBtn && showCenterBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line1.setVisibility(View.VISIBLE);
            btn_center.setBackgroundResource(R.drawable.alertdialog_center_selector);
            btn_center.setVisibility(View.VISIBLE);
            img_line2.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }
}

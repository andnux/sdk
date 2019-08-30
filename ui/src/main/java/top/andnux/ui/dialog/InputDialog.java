package top.andnux.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import top.andnux.ui.R;

public class InputDialog {

    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private EditText input_view;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;
    private boolean showTitle = false;
    private boolean showInput = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public interface OnInputListener {
        void onInput(Dialog dialog, String text);
    }

    public InputDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        builder();
    }

    private void builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_inputialog, null);
        lLayout_bg = view.findViewById(R.id.lLayout_bg);
        txt_title = view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        input_view = view.findViewById(R.id.input_view);
        input_view.setVisibility(View.GONE);
        btn_neg = view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public InputDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("温馨提示");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public InputDialog setHint(String msg) {
        showInput = true;
        input_view.setHint(msg);
        return this;
    }

    public InputDialog setText(String msg) {
        showInput = true;
        input_view.setText(msg);
        return this;
    }

    public InputDialog setCancelListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
        return this;
    }

    public InputDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public InputDialog setPositiveButton(String text,
                                         final OnInputListener listener) {
        showPosBtn = true;
        btn_pos.setText(text);
        btn_pos.setOnClickListener(v -> {
            if (listener != null){
                listener.onInput(dialog,input_view.getText().toString());
            }
        });
        return this;
    }

    public InputDialog setNegativeButton(String text,
                                         final View.OnClickListener listener) {
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
        if (!showTitle && !showInput) {
            txt_title.setText("⚠️警告");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showInput) {
            input_view.setVisibility(View.VISIBLE);
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
            img_line.setVisibility(View.VISIBLE);
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

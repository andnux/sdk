package top.andnux.ui;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import top.andnux.ui.stateview.StateButton;

/**
 * 倒计时 显示内容
 */
public class CountDownButton extends StateButton {

    private CountDownTimer timer;
    private int length = 60;

    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void start() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(1000 * length, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setText((millisUntilFinished / 1000) + getContext().getString(R.string.re_send));
            }

            @Override
            public void onFinish() {
                setEnabled(true);
                setText(getContext().getString(R.string.re_send_code));
            }
        };
        timer.start();
        setEnabled(false);
    }

    public void oncancel() {
        timer.cancel();
    }
}

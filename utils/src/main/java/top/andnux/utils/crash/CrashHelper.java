package top.andnux.utils.crash;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.text.format.DateFormat;

import androidx.appcompat.app.AlertDialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import top.andnux.utils.common.PhoneUtil;

/**
 * 处理崩溃 异常
 */
public class CrashHelper implements Thread.UncaughtExceptionHandler {

    private static CrashHelper mCrashHelper;
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Context mContext;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath();

    private CrashHelper() {
    }

    public static CrashHelper getCrashHelper() {
        if (mCrashHelper == null) {
            synchronized (CrashHelper.class) {
                if (mCrashHelper == null) {
                    mCrashHelper = new CrashHelper();
                }
            }
        }
        return mCrashHelper;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        Logger.getInstance().init(path);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        final String stacktrace = result.toString();
        final String fileName = DateFormat.format("yyyy年MM月dd日 HH时mm分ss秒", System.currentTimeMillis()) + ".txt";
        String stringBuffer = PhoneUtil.getPhoneInfo(mContext);
        Logger.getInstance().e("exception", stringBuffer,true,fileName);
        if (defaultUEH != null){
            defaultUEH.uncaughtException(thread, ex);
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false)
                        .setMessage("程序崩溃了...")
                        .setNeutralButton("我知道了", (dialog, which) -> System.exit(0))
                        .create().show();
                Looper.loop();
            }
        }.start();
    }
}

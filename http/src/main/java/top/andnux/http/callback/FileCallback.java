package top.andnux.http.callback;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import top.andnux.http.core.HttpResponse;

public abstract class FileCallback implements HttpCallback {

    private String mFilePath;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public FileCallback(String filePath) {
        mFilePath = filePath;
    }

    public abstract void onProgress(long current, long total);

    public abstract void onSuccess(File file);

    @Override
    public void onSuccess(HttpResponse response) {
        try {
            FileOutputStream fos = new FileOutputStream(mFilePath);
            InputStream input = response.getInputStream();
            byte[] b = new byte[1024 * 4];
            int length;
            int current = 0;
            while ((length = input.read(b)) > 0) {
                fos.write(b, 0, length);
                current += length;
                int finalCurrent = current;
                mHandler.post(() -> onProgress(finalCurrent, response.getContentLength()));
            }
            mHandler.post(() -> onSuccess(new File(mFilePath)));
            fos.flush();
            input.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

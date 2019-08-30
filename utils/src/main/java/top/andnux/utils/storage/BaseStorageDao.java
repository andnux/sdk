package top.andnux.utils.storage;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import top.andnux.utils.storage.annotation.FileName;

public abstract class BaseStorageDao<T> implements StorageDao<T> {

    protected final ExecutorService mService;
    protected final Class<T> mClass;
    protected final Context mContext;
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
    protected String fileName;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void changeFileName(String fileName) {
        this.fileName = fileName;
        init();
    }

    public String getFileName() {
        return fileName;
    }

    protected abstract void init();

    public BaseStorageDao(Context context, Class<T> clazz) {
        mClass = clazz;
        mContext = context;
        mService = Executors.newSingleThreadExecutor();
        FileName annotation = clazz.getAnnotation(FileName.class);
        String value = clazz.getCanonicalName();
        if (annotation != null && !TextUtils.isEmpty(annotation.value())) {
            value = annotation.value();
        }
        changeFileName(value);
    }

    @Override
    public void save(final T data, final StorageListener<T> listener) {
        mService.execute(() -> {
            try {
                save(data);
                mHandler.post(() -> {
                    if (listener != null) listener.onSuccess(data);
                });
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.post(() -> {
                    if (listener != null) listener.onFail(e);
                });
            }
            mHandler.post(() -> {
                if (listener != null) listener.onComplete();
            });
        });

    }

    @Override
    public void load(final StorageListener<T> listener) {
        mService.execute(() -> {
            try {
                T data = load();
                if (data != null) {
                    mHandler.post(() -> {
                        if (listener != null) listener.onSuccess(data);
                    });
                } else {
                    mHandler.post(() -> {
                        if (listener != null) listener.onFail(new RuntimeException("读取到的对象为空"));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.post(() -> {
                    if (listener != null) listener.onFail(e);
                });
            }
            mHandler.post(() -> {
                if (listener != null) listener.onComplete();
            });
        });
    }

    @Override
    public void shutdown() {
        if (mService != null && !mService.isShutdown()) {
            mService.shutdown();
        }
    }
}

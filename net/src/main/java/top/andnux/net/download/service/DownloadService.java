package top.andnux.net.download.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import top.andnux.net.download.bean.FileInfo;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";
    //初始化
    private static final int MSG_INIT = 0x2;
    //开始下载
    public static final String ACTION_START = "ACTION_START";
    //暂停下载
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    //结束下载
    public static final String ACTION_FINISHED = "ACTION_FINISHED";
    //更新UI
    public static final String ACTION_UPDATE = "ACTION_UPDATE";

    private DownloadHandler mHandler = new DownloadHandler(this);

    private static class DownloadHandler extends Handler {

        private WeakReference<DownloadService> mService;

        public DownloadHandler(DownloadService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    FileInfo fileinfo = (FileInfo) msg.obj;
                    Log.e("mHandler--fileinfo:", fileinfo.toString());
                    break;
            }
        }
    }

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            if (fileInfo != null) {
                Log.e(TAG, "onStartCommand: ACTION_START-" + fileInfo.toString());
                new InitThread(fileInfo).start();
            }
        } else if (ACTION_PAUSE.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            if (fileInfo != null) {
                Log.e(TAG, "onStartCommand:ACTION_PAUSE- " + fileInfo.toString());
            }
        }
    }


    class InitThread extends Thread {

        private FileInfo mFileInfo;

        public InitThread(FileInfo fileInfo) {
            mFileInfo = fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn;
            RandomAccessFile raf;
            try {
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                int length = -1;
                Log.e("getResponseCode==", conn.getResponseCode() + "");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //获取文件长度
                    length = conn.getContentLength();
                    Log.e("length==", length + "");
                }
                if (length < 0) {
                    return;
                }
                File dir = new File(mFileInfo.getDir());
                if (!dir.exists()) {
                    if (!dir.mkdir()) {
                        return;
                    }
                }
                File file = new File(dir, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                //设置本地文件长度
                raf.setLength(length);
                mFileInfo.setLength(length);
                Log.e("tFileInfo.getLength==", mFileInfo.getLength() + "");
                mHandler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
                raf.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

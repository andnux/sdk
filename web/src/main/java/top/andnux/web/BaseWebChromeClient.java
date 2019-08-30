package top.andnux.web;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.util.Date;

import top.andnux.compat.UriCompat;
import top.andnux.ui.dialog.ActionSheetDialog;
import top.andnux.ui.dialog.AlertDialog;
import top.andnux.ui.dialog.InputDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by andnux on 2018/4/11.
 */

public class BaseWebChromeClient extends WebChromeClient implements WrapListener {

    private WarpFragment fragment;
    private ValueCallback<Uri> uriValueCallback;
    private ValueCallback<Uri[]> uriValueCallbacks;
    private Activity mContext;
    private ProgressBar mProgressBar;
    private static final int REQ_CHOOSE = 0x88;
    private String mCameraFilePath = "";
    private String message;
    private GeolocationPermissions.Callback mCallback;
    private String mOrigin;

    public BaseWebChromeClient(Activity context, ProgressBar progress) {
        mContext = context;
        mProgressBar = progress;
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            fragment = WarpFragment.getInstance(this);
            transaction.add(android.R.id.content, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        uriValueCallbacks = filePathCallback;
        selectPictures();
        return true;
    }


    private void selectPictures() {
        new ActionSheetDialog(mContext)
                .addSheetItem(mContext.getString(R.string.camera),
                        ActionSheetDialog.SheetItemColor.Blue, which -> {
                            int ic = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
                            int is = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (ic == PackageManager.PERMISSION_GRANTED && is == PackageManager.PERMISSION_GRANTED) {
                                fragment.startActivityForResult(createCameraIntent(), REQ_CHOOSE);
                            } else {
                                fragment.requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, 0x88);
                                message = mContext.getString(R.string.no_camera);
                                reset();
                            }
                        }).addSheetItem(mContext.getString(R.string.album),
                ActionSheetDialog.SheetItemColor.Blue, which -> {
                    int i = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (i == PackageManager.PERMISSION_GRANTED) {
                        fragment.startActivityForResult(createDefaultOpenableIntent(), REQ_CHOOSE);
                    } else {
                        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x88);
                        message = mContext.getString(R.string.no_album);
                        reset();
                    }
                })
                .setCancelListener(dialog -> reset())
                .setTitle(mContext.getString(R.string.chose)).show();
    }

    private Intent createDefaultOpenableIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return i;
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mContext.getPackageManager()) != null) {
            mCameraFilePath = createFile(mContext).getAbsolutePath();
            Uri fromFile = UriCompat.fromFile(mContext, new File(mCameraFilePath));
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fromFile);
        }
        return cameraIntent;
    }

    private File createFile(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +
                    File.separator + timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir, timeStamp + ".jpg");
        }
        return file;
    }

    private void update(Uri[] uris) {
        if (uriValueCallbacks != null
                && uris[0] != null) {
            uriValueCallbacks.onReceiveValue(uris);
            uriValueCallbacks = null;
        }
        if (uriValueCallback != null
                && uris[0] != null) {
            uriValueCallback.onReceiveValue(uris[0]);
            uriValueCallback = null;
        }
    }

    private void reset() {
        if (uriValueCallbacks != null) {
            uriValueCallbacks.onReceiveValue(null);
            uriValueCallbacks = null;
        }
        if (uriValueCallback != null) {
            uriValueCallback.onReceiveValue(null);
            uriValueCallback = null;
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
        mOrigin = origin;
        mCallback = callback;
        new AlertDialog(mContext)
                .setTitle(mContext.getString(R.string.location_info))
                .setMessage(origin + mContext.getString(R.string.get_your_location))
                .setCancelable(true)
                .setPositiveButton(mContext.getString(R.string.allow), v -> {
                    int i1 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
                    int i2 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (i1 == PackageManager.PERMISSION_GRANTED && i2 == PackageManager.PERMISSION_GRANTED) {
                        callback.invoke(origin, true, true);
                    } else {
                        fragment.requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }, 100);
                    }
                })
                .setCancelable(false)
                .setNegativeButton(mContext.getString(R.string.refuse), v -> callback.invoke(origin, false, true))
                .show();
    }

    /**
     * 当前 WebView 加载网页进度
     *
     * @param view
     * @param newProgress
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (mProgressBar == null) {
            return;
        }
        if (newProgress == 100) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
            mProgressBar.setProgress(newProgress);//设置进度值
        }
    }

    /**
     * 接收web页面的 Title
     *
     * @param view
     * @param title
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    /**
     * 接收web页面的icon
     *
     * @param view
     * @param icon
     */
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    /**
     * Js 中调用 alert() 函数，产生的对话框
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        new AlertDialog(mContext)
                .setTitle("")
                .setMsg(message)
                .setPositiveButton(mContext.getString(R.string.sure), v -> {
                    result.confirm();
                }).show();
        return true;
    }

    /**
     * 处理 Js 中的 Confirm 对话框
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        new AlertDialog(mContext)
                .setTitle("")
                .setMsg(message)
                .setPositiveButton(mContext.getString(R.string.sure), v -> {
                    result.confirm();
                }).setNegativeButton(mContext.getString(R.string.cancel), v -> {
            result.cancel();
        }).show();
        return true;
    }

    /**
     * 处理 JS 中的 Prompt对话框
     *
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     * @return
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String
            defaultValue, JsPromptResult result) {
        new InputDialog(mContext)
                .setTitle(message)
                .setText(defaultValue)
                .setNegativeButton(mContext.getString(R.string.cancel), v -> {
                    result.cancel();
                })
                .setPositiveButton(mContext.getString(R.string.sure), (dialog, text) -> {
                    result.confirm(text);
                    dialog.dismiss();
                })
                .show();
        return true;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CHOOSE && data != null && resultCode == RESULT_OK) {
            Uri[] uris = new Uri[1];
            uris[0] = data.getData();
            update(uris);
        } else {
            reset();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != -1) {
                    //T.showShort(mContext,"权限设置成功");
                } else {
                    //T.showShort(mContext,"拒绝权限");
                    // 权限被拒绝，弹出dialog 提示去开启权限
                    showPermissions(message);
                    break;
                }
            }
        } else if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != -1) {
                    //T.showShort(mContext,"权限设置成功");
                    mCallback.invoke(mOrigin, true, true);
                } else {
                    //T.showShort(mContext,"拒绝权限");
                    // 权限被拒绝，弹出dialog 提示去开启权限
                    showPermissions(message);
                    break;
                }
            }
        }
    }

    private void toSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        mContext.startActivity(intent);
    }

    private void showPermissions(String text) {
        new AlertDialog(mContext)
                .setTitle("")
                .setMsg(text)
                .setPositiveButton(mContext.getString(R.string.sure), v -> {
                    toSetting();
                }).setNegativeButton(mContext.getString(R.string.cancel), v -> {
        }).show();
    }
}
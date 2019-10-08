package top.andnux.update.installer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import java.io.File;

import top.andnux.utils.fragment.FragmentProxy;

/**
 * 普通apk安装的实现
 */
public class ApkInstaller implements Installer {

    private static final int REQUEST_CODE_APP_INSTALL = 0x999;
    private String mApkPath;
    private Context mContext;

    @Override
    public void install(Context context, String file) throws Exception {
        mApkPath = file;
        mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = isHasInstallPermissionWithO(context);
            if (!hasInstallPermission) {
                startInstallPermissionSettingActivity(context);
                return;
            }
        }
        installApk(context, file);
    }

    private void installApk(Context context, String downloadApk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(downloadApk);
        Log.i("TAG", "安装路径==" + downloadApk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        FragmentActivity activity = (FragmentActivity) context;
        FragmentProxy.with(activity)
                .setListener(new FragmentProxy.ProxyListener() {
                    @Override
                    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

                    }

                    @Override
                    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_APP_INSTALL) {
                            try {
                                install(mContext, mApkPath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }
}

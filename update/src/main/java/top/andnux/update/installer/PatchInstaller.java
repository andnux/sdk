package top.andnux.update.installer;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import top.andnux.libbspatch.Bspatch;

/**
 * 差分包安装的实现
 */
public class PatchInstaller implements Installer {

    @Override
    public void install(Context context, String file) throws Exception {
        String apkDir = context.getApplicationInfo().sourceDir;
        File cacheDir = context.getExternalCacheDir();
        String newApk;
        if (cacheDir != null) {
            newApk = new File(cacheDir,
                    System.currentTimeMillis() + ".apk").getAbsolutePath();
        } else {
            newApk = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".apk").getAbsolutePath();
        }
        if (Bspatch.bspatch(apkDir, newApk, file) != 0) {
            throw new Exception("apk合成失败，请下载完整版");
        }
        new ApkInstaller().install(context, newApk);
    }
}

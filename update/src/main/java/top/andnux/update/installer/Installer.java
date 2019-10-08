package top.andnux.update.installer;

import android.content.Context;

/**
 * 安装器
 */
public interface Installer {

    void install(Context context, String file) throws Exception;

}

package top.andnux.net.netstate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager service = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (service == null) return false;
        NetworkInfo[] networkInfos = service.getAllNetworkInfo();
        for (NetworkInfo info : networkInfos) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static NetState getNetState(Context context) {
        ConnectivityManager service = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (service == null) return NetState.NONE;
        NetworkInfo activeNetworkInfo = service.getActiveNetworkInfo();
        if (activeNetworkInfo == null) return NetState.NONE;
        int type = activeNetworkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            return NetState.MOBILE;
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetState.WIFI;
        }
        return NetState.NONE;
    }
}

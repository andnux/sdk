package top.andnux.net.cache;

import android.content.Context;

public abstract class AbstractCache implements Cache {

    private Context mContext;

    public AbstractCache(Context context) {
        mContext = context;
    }
}

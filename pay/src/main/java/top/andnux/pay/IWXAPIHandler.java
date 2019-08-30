package top.andnux.pay;

import android.app.Activity;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

public interface IWXAPIHandler {

    void onReq(Activity activity, BaseReq var1);

    void onResp(Activity activity,BaseResp var1);
}
package top.andnux.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;

public class PayManager implements IWXAPIEventHandler, IWXAPIHandler {

    private static final PayManager ourInstance = new PayManager();

    public static PayManager getInstance() {
        return ourInstance;
    }

    private IWXAPI mIWxApi;
    private WeakReference<Activity> mReference;
    private IWXAPIHandler mHandler;
    private IPayListener mPayListener;

    public IPayListener getPayListener() {
        return mPayListener;
    }

    public void setPayListener(IPayListener payListener) {
        mPayListener = payListener;
    }

    private PayManager() {
    }

    public void init(Context context, String appId) {
        mIWxApi = WXAPIFactory.createWXAPI(context, null);
        mIWxApi.registerApp(appId);
        mHandler = this;
    }


    public IWXAPI getIWxApi() {
        return mIWxApi;
    }

    public void sendReq(BaseReq baseReq) {
        mIWxApi.sendReq(baseReq);
    }

    public void handleIntent(Activity activity, Intent intent) {
        mReference = new WeakReference<>(activity);
        mIWxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Activity activity = mReference.get();
        if (mHandler != null) {
            mHandler.onReq(activity, baseReq);
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Activity activity = mReference.get();
        if (mHandler != null) {
            mHandler.onResp(activity, baseResp);
        }
    }

    @Override
    public void onReq(Activity activity, BaseReq baseReq) {

    }

    @Override
    public void onResp(Activity activity, BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = resp.errCode;
            if (errCode == 0) {
                if (mPayListener != null) mPayListener.onSuccess();
            } else if (errCode == -1) {
                if (mPayListener != null) mPayListener.onError();
            } else if (errCode == -2) {
                if (mPayListener != null) mPayListener.onCancel();
            }

        } else if (resp instanceof SendAuth.Resp) {

        }
    }
}

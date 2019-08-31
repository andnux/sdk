package top.andnux.zbarui;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

import top.andnux.zbarui.utils.PermissionConstants;
import top.andnux.zbarui.utils.PermissionUtils;

/**
 * Created by Bert on 2017/9/22.
 */

public class ScanCodeManager {

    private static ScanCodeManager instance;
    private ScanCodeConfig options;

    public OnScanResultCallback resultCallback;

    public synchronized static ScanCodeManager getInstance() {
        if(instance == null)
            instance = new ScanCodeManager();
        return instance;
    }

    public OnScanResultCallback getResultCallback() {
        return resultCallback;
    }


    public ScanCodeManager init(ScanCodeConfig options) {
        this.options = options;
        return this;
    }

    public void startScan(final Activity activity, OnScanResultCallback resultCall){

        if (options == null) {
            options = new ScanCodeConfig.Builder().create();
        }

        PermissionUtils.permission(activity, PermissionConstants.CAMERA,PermissionConstants.STORAGE)
                .rationale(shouldRequest -> shouldRequest.again(true))
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        Intent intent = new Intent(activity, ScanCodeActivity.class);
                        intent.putExtra(ScanCodeConfig.EXTRA_THIS_CONFIG, options);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        Toast.makeText(activity,"摄像头权限被拒绝！",Toast.LENGTH_SHORT).show();

                    }
                }).request();



        // 绑定图片接口回调函数事件
        resultCallback = resultCall;
    }



    public interface OnScanResultCallback {
        /**
         * 处理成功
         * 多选
         *
         * @param result
         */
        void onScanSuccess(String result);

    }
}

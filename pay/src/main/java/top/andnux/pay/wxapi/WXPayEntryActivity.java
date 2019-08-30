package top.andnux.pay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import top.andnux.pay.PayManager;

public class WXPayEntryActivity extends Activity {

    private PayManager instance = PayManager.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance.handleIntent(this, getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        instance.handleIntent(this, intent);
    }
}

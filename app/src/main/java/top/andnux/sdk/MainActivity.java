package top.andnux.sdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import top.andnux.compat.ToastCompat;
import top.andnux.zbarui.ScanCodeConfig;
import top.andnux.zbarui.ScanCodeManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(this);
    }

    public void onClick(View view) {
        ScanCodeManager instance = ScanCodeManager.getInstance();
        ScanCodeConfig options = new ScanCodeConfig();
        options.TITLE_BACKGROUND_COLOR = ContextCompat.getColor(this,R.color.colorPrimary);
        options.LINE_COLOR = ContextCompat.getColor(this,R.color.colorPrimary);
        options.CORNER_COLOR = ContextCompat.getColor(this,R.color.colorPrimary);
        instance.init(options);
        instance.startScan(MainActivity.this, new ScanCodeManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(String result) {
                ToastCompat.showShortText(MainActivity.this,result);
            }
        });
    }
}

package top.andnux.sdk;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import top.andnux.net.HttpManager;
import top.andnux.net.callback.StringCallback;
import top.andnux.net.core.HttpRequest;
import top.andnux.sqlite.QueryWhere;
import top.andnux.sqlite.SQLiteDao;
import top.andnux.sqlite.SQLiteManager;
import top.andnux.sqlite.http.SQLiteHttpHelper;
import top.andnux.ui.dialog.BottomSheetDialog;
import top.andnux.ui.dialog.DividerDecoration;
import top.andnux.ui.image.BigImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(this);
        SQLiteManager.init(getDatabasePath("sdk.db"));
        SQLiteDao<UserEntity> dao = SQLiteManager.getInstance().getDao(UserEntity.class);
        UserEntity t = new UserEntity();
        t.setAge(new Random().nextInt(100));
        t.setName("张春林");
        t.setIdCard(UUID.randomUUID().toString().replace("-", ""));
        dao.insert(t);
        QueryWhere where = new QueryWhere(UserEntity.class);
        List<UserEntity> query = dao.query(where);
        for (UserEntity entity : query) {
            Log.e("TAG", entity.toString());
        }
        SQLiteHttpHelper.init(getApplication(), BuildConfig.DEBUG,
                getDatabasePath("sdk.db"));
        BigImageView imageView = findViewById(R.id.image);
        try {
            InputStream image = getResources().openRawResource(R.raw.contacts);
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpRequest httpRequest = HttpManager.getInstance().newHttpRequest();
        httpRequest.setUrl("https://bitcoinfees.earn.com/api/v1/fees/recommended");
        httpRequest.setHttpCallback(new StringCallback() {
            @Override
            public void onSuccess(String string) {
                Log.d(TAG, "onSuccess() called with: string = [" + string + "]");
            }

            @Override
            public void onFail(Exception e) {
                Log.d(TAG, "onFail() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
            }
        });
        HttpManager.getInstance().sendRequest(httpRequest);
    }

    public void onClick(View view) {
//        ScanCodeManager instance = ScanCodeManager.getInstance();
//        ScanCodeConfig options = new ScanCodeConfig();
//        options.TITLE_BACKGROUND_COLOR = ContextCompat.getColor(this, R.color.colorPrimary);
//        options.LINE_COLOR = ContextCompat.getColor(this, R.color.colorPrimary);
//        options.CORNER_COLOR = ContextCompat.getColor(this, R.color.colorPrimary);
//        instance.init(options);
//        instance.startScan(MainActivity.this, result -> ToastCompat.showShortText(MainActivity.this, result));
        new BottomSheetDialog(this)
                .setTitle("标题")
                .setTitleColor(Color.BLACK)
                .setBackgroundColor(Color.TRANSPARENT)
                .addItemDecoration(new DividerDecoration(this,DividerDecoration.ALL))
                .addSheetItem("相机", BottomSheetDialog.SheetItemColor.Blue, which -> {

                }).addSheetItem("相册", BottomSheetDialog.SheetItemColor.Blue, which -> {

                }).show();
    }
}

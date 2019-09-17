package top.andnux.sdk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import top.andnux.compat.ToastCompat;
import top.andnux.sqlite.OrderBy;
import top.andnux.sqlite.QueryWhere;
import top.andnux.sqlite.SQLiteDao;
import top.andnux.sqlite.SQLiteManager;
import top.andnux.sqlite.http.SQLiteHttpHelper;
import top.andnux.zbarui.ScanCodeConfig;
import top.andnux.zbarui.ScanCodeManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        SQLiteHttpHelper.init(getApplication(),BuildConfig.DEBUG,
                getDatabasePath("sdk.db"));
    }

    public void onClick(View view) {
        ScanCodeManager instance = ScanCodeManager.getInstance();
        ScanCodeConfig options = new ScanCodeConfig();
        options.TITLE_BACKGROUND_COLOR = ContextCompat.getColor(this, R.color.colorPrimary);
        options.LINE_COLOR = ContextCompat.getColor(this, R.color.colorPrimary);
        options.CORNER_COLOR = ContextCompat.getColor(this, R.color.colorPrimary);
        instance.init(options);
        instance.startScan(MainActivity.this, result -> ToastCompat.showShortText(MainActivity.this, result));
    }
}

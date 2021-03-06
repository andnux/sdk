package top.andnux.zbarui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.soundcloud.android.crop.Crop;

import java.io.File;

import top.andnux.zbar.Symbol;
import top.andnux.zbarui.utils.GetPathFromUri;
import top.andnux.zbarui.utils.QRUtils;
import top.andnux.zbarui.view.ScanView;
import top.andnux.zbarui.view.VerticalSeekBar;

public class ScanCodeActivity extends Activity implements View.OnClickListener {

    private CameraPreview cp;
    private SoundPool soundPool;
    private ScanView sv;
    private ImageView mo_scanner_back;
    private ImageView iv_flash;
    private TextView tv_album;
    private TextView textDialog;
    private TextView tv_title;
    private FrameLayout fl_title;
    private TextView tv_des;
    private ScanCodeConfig options;
    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_PHOTO_CUT = 2;
    private Uri uricropFile;
    private String cropTempPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "cropQr.jpg";
    private VerticalSeekBar vsb_zoom;


    private float oldDist = 1f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                options = (ScanCodeConfig) extras.get(ScanCodeConfig.EXTRA_THIS_CONFIG);
            }
            if (options != null) {
                switch (options.getSCREEN_ORIENTATION()) {
                    case ScanCodeConfig.SCREEN_LANDSCAPE:
                        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        }
                        break;
                    case ScanCodeConfig.SCREEN_PORTRAIT:
                        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        }
                        break;
                    case ScanCodeConfig.SCREEN_SENSOR:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        break;
                    default:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }

                Symbol.scanType = options.getScan_type();
                Symbol.scanFormat = options.getCustombarcodeformat();
                Symbol.is_only_scan_center = options.isOnly_center();
                Symbol.is_auto_zoom = options.isAuto_zoom();
                Symbol.screenWidth = QRUtils.getInstance().getScreenWidth(this);
                Symbol.screenHeight = QRUtils.getInstance().getScreenHeight(this);
            }

            setContentView(R.layout.activity_scan_code);
            initView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (cp != null) {
            cp.setScanCallback(resultCallback);
            cp.start();
        }
        sv.onResume();
    }

    private void initView() {
        cp = (CameraPreview) findViewById(R.id.cp);
        //bi~
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, options.getDing_path(), 1);

        sv = (ScanView) findViewById(R.id.sv);
        sv.setType(options.getScan_view_type());
        sv.startScan();

        mo_scanner_back = findViewById(R.id.mo_scanner_back);
        mo_scanner_back.setOnClickListener(this);

        iv_flash = findViewById(R.id.iv_flash);
        iv_flash.setOnClickListener(this);

        tv_album = findViewById(R.id.tv_album);
        tv_album.setOnClickListener(this);

        tv_title = findViewById(R.id.tv_title);
        fl_title = findViewById(R.id.fl_title);
        tv_des = findViewById(R.id.tv_des);

        vsb_zoom = findViewById(R.id.vsb_zoom);

        tv_album.setVisibility(options.isShow_light() ? View.VISIBLE : View.GONE);
        fl_title.setVisibility(options.isShow_title() ? View.VISIBLE : View.GONE);
        iv_flash.setVisibility(options.isShow_light() ? View.VISIBLE : View.GONE);
        tv_album.setVisibility(options.isShow_album() ? View.VISIBLE : View.GONE);
        tv_des.setVisibility(options.isShow_des() ? View.VISIBLE : View.GONE);
        vsb_zoom.setVisibility(options.isShow_zoom() ? View.VISIBLE : View.GONE);

        tv_des.setText(options.getDes_text());
        tv_title.setText(options.getTitle_text());
        fl_title.setBackgroundColor(options.getTITLE_BACKGROUND_COLOR());
        tv_title.setTextColor(options.getTITLE_TEXT_COLOR());

        sv.setCornerColor(options.getCORNER_COLOR());
        sv.setLineSpeed(options.getLine_speed());
        sv.setLineColor(options.getLINE_COLOR());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setSeekBarColor(vsb_zoom, options.getCORNER_COLOR());
        }
        vsb_zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cp.setZoom((progress / 100f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setSeekBarColor(SeekBar seekBar, int color) {
        seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    private ScanCallback resultCallback = new ScanCallback() {
        @Override
        public void onScanResult(String result) {
            if (options.isPlay_sound()) {
                soundPool.play(1, 1, 1, 0, 0, 1);
            }
            if (cp != null) {
                cp.setFlash(false);
            }
            ScanCodeManager.getInstance().getResultCallback().onScanSuccess(result);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cp != null) {
            cp.setFlash(false);
            cp.stop();
        }
        soundPool.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cp != null) {
            cp.stop();
        }
        sv.onPause();
    }

    /**
     * 从相册选择
     */
    private void fromAlbum() {
        if (QRUtils.getInstance().isMIUI()) {//是否是小米设备,是的话用到弹窗选取入口的方法去选取
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.scan_image)), REQUEST_IMAGE_GET);
        } else {//直接跳到系统相册去选取
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            } else {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
            }
            startActivityForResult(Intent.createChooser(intent, getString(R.string.scan_image)), REQUEST_IMAGE_GET);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_album) {
            fromAlbum();
        } else if (v.getId() == R.id.iv_flash) {
            if (cp != null) {
                cp.setFlash();
            }
        } else if (v.getId() == R.id.mo_scanner_back) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            switch (requestCode) {
                case REQUEST_IMAGE_GET:
                    if (options.isNeed_crop()) {
                        cropPhoto(data.getData());
                    } else {
                        recognitionLocation(data.getData());
                    }
                    break;
                case Crop.REQUEST_CROP:
                    recognitionLocation(uricropFile);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void recognitionLocation(Uri uri) {
        final String imagePath = GetPathFromUri.getPath(this, uri);
        textDialog = showProgressDialog();
        textDialog.setText(getString(R.string.ter_on));
        new Thread(() -> {
            try {
                if (TextUtils.isEmpty(imagePath)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.image_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                //优先使用zbarui识别一次二维码
                final String qrcontent = QRUtils.getInstance().decodeQRcode(imagePath);
                runOnUiThread(() -> {
                    if (!TextUtils.isEmpty(qrcontent)) {
                        closeProgressDialog();
                        ScanCodeManager.getInstance().getResultCallback().onScanSuccess(qrcontent);
                        delete(cropTempPath);//删除裁切的临时文件
                        finish();
                    } else {
                        //尝试用zxing再试一次识别二维码
                        final String qrcontent1 = QRUtils.getInstance().decodeQRcodeByZxing(imagePath);
                        if (!TextUtils.isEmpty(qrcontent1)) {
                            closeProgressDialog();
                            ScanCodeManager.getInstance().getResultCallback().onScanSuccess(qrcontent1);
                            delete(cropTempPath);//删除裁切的临时文件
                            finish();
                        } else {
                            //再试试是不是条形码
                            try {
                                String barcontent = QRUtils.getInstance().decodeBarcode(imagePath);
                                if (!TextUtils.isEmpty(barcontent)) {
                                    closeProgressDialog();
                                    ScanCodeManager.getInstance().getResultCallback().onScanSuccess(barcontent);
                                    delete(cropTempPath);//删除裁切的临时文件
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.recognition_failure), Toast.LENGTH_SHORT).show();
                                    closeProgressDialog();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), getString(R.string.identifying_anomalies), Toast.LENGTH_SHORT).show();
                                closeProgressDialog();
                                e.printStackTrace();
                            }

                        }

                    }
                });
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.identifying_anomalies), Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        }).start();
    }

    public void cropPhoto(Uri uri) {
        uricropFile = Uri.parse("file://" + "/" + cropTempPath);
        Crop.of(uri, uricropFile).asSquare().start(this);
    }

    private boolean delete(String delFile) {
        File file = new File(delFile);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    private AlertDialog progressDialog;

    public TextView showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setCancelable(false);
        View view = View.inflate(this, R.layout.dialog_loading, null);
        builder.setView(view);
        ProgressBar pb_loading = view.findViewById(R.id.pb_loading);
        TextView tv_hint = view.findViewById(R.id.tv_hint);
        if (Build.VERSION.SDK_INT >= 23) {
            pb_loading.setIndeterminateTintList(getColorStateList(R.color.dialog_pro_color));
        }
        progressDialog = builder.create();
        progressDialog.show();

        return tv_hint;
    }

    public void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (options.isFinger_zoom()) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = QRUtils.getInstance().getFingerSpacing(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 2) {
                        float newDist = QRUtils.getInstance().getFingerSpacing(event);
                        if (newDist > oldDist) {
                            cp.handleZoom(true);
                        } else if (newDist < oldDist) {
                            cp.handleZoom(false);
                        }
                        oldDist = newDist;
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);

    }


}

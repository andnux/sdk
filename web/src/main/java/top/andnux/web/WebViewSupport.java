package top.andnux.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import top.andnux.compat.UriCompat;
import top.andnux.ui.dialog.ActionSheetDialog;
import top.andnux.utils.common.ToastUtil;

class WebViewSupport {

    static void showSaveImage(Activity context, String url) {
        context.runOnUiThread(() -> {
            new ActionSheetDialog(context)
                    .addSheetItem(context.getString(R.string.download_image),
                            ActionSheetDialog.SheetItemColor.Blue, which -> {
                                new Thread(() -> saveImage(context, url, false)).start();
                            }).addSheetItem(context.getString(R.string.share_image),
                    ActionSheetDialog.SheetItemColor.Blue, which -> {
                        new Thread(() -> saveImage(context, url, true)).start();
                    }).setTitle(context.getString(R.string.chose)).show();
        });
    }

    private static void saveImage(Activity context, String data, boolean share) {
        try {

            Bitmap bitmap = webData2bitmap(context, data);
            if (bitmap != null) {
                save2Album(context, bitmap, new SimpleDateFormat("yyyyMMddHHmmss",
                        Locale.getDefault()).format(new Date()) + ".jpg", share);
            } else {
                context.runOnUiThread(() -> ToastUtil.success(context, context.getString(R.string.save_success)));
            }
        } catch (Exception e) {
            context.runOnUiThread(() -> ToastUtil.error(context, context.getString(R.string.save_error)));
            e.printStackTrace();
        }
    }

    private static Uri getImageUri(Activity context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage,
                "Title", null);
        return Uri.parse(path);
    }

    private static Bitmap webData2bitmap(Activity context, String data) {
        byte[] imageBytes = Base64.decode(data.split(",")[1], Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private static void save2Album(Activity context, Bitmap bitmap, String fileName, boolean share) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            context.runOnUiThread(() -> {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                if (!share) {
                    ToastUtil.success(context, context.getString(R.string.save_success));
                } else {
                    Uri imageUri = Uri.fromFile(file);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/*");
                    if (file.isFile() && file.exists()) {
                        Uri uri = UriCompat.fromFile(context, file);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    }
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to)));
                }
            });
        } catch (Exception e) {
            context.runOnUiThread(() -> ToastUtil.error(context, context.getString(R.string.save_error)));
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ignored) {
            }
        }
    }
}

package top.andnux.utils.common;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageUtil {

    public interface ImageLoadFinish {
        void onFinish(List<Map<String, Object>> imageList);
    }

    private ImageLoadFinish imageLoadFinish;

    public void setImageLoadFinish(ImageLoadFinish imageLoadFinish) {
        this.imageLoadFinish = imageLoadFinish;
    }

    private List<Map<String, Object>> imageList = new ArrayList<>();

    private static final ImageUtil ourInstance = new ImageUtil();

    public static ImageUtil getInstance() {
        return ourInstance;
    }

    private ImageUtil() {
    }

    public List<Map<String, Object>> getImageList() {
        return imageList;
    }

    public void getAllImage(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Map<String, Object> map = new HashMap<>();
                String path = new String(data, 0, data.length - 1);
                map.put("path", path);
                imageList.add(map);
                LogUtil.e(path);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        if (imageLoadFinish != null) {
            imageLoadFinish.onFinish(imageList);
        }
    }
}

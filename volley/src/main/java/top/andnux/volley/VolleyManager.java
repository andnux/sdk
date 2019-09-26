package top.andnux.volley;

import android.app.Application;

import top.andnux.volley.toolbox.ImageLoader;
import top.andnux.volley.toolbox.Volley;

public class VolleyManager {

    private static RequestQueue sRequestQueue;
    private static ImageLoader sImageLoader;

    public static void init(Application application) {
        sRequestQueue = Volley.newRequestQueue(application);
        sImageLoader = new ImageLoader(application);
    }

    public static ImageLoader getImageLoader() {
        if (sImageLoader == null) {
            throw new IllegalArgumentException("please call VolleyManager.init(application)");
        }
        return sImageLoader;
    }

    public static RequestQueue getRequestQueue() {
        if (sRequestQueue == null) {
            throw new IllegalArgumentException("please call VolleyManager.init(application)");
        }
        return sRequestQueue;
    }
}

package top.andnux.base.image;

public class ImageLoaderManager {

    private static final ImageLoaderManager ourInstance = new ImageLoaderManager();

    public static ImageLoaderManager getInstance() {
        return ourInstance;
    }

    private ImageLoader mImageLoader;

    private ImageLoaderManager() {
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }
}

package demo.xdw.nwd.com.workdemo.network;


import demo.xdw.nwd.com.workdemo.network.provider.IImageLoaderProvider;
import demo.xdw.nwd.com.workdemo.network.provider.PicassoImageLoaderProvider;

public class ImageLoader {

    private static volatile IImageLoaderProvider mProvider;

    public static IImageLoaderProvider getProvider() {
        if (mProvider == null) {
            synchronized (ImageLoader.class) {
                if (mProvider == null) {
                    mProvider = new PicassoImageLoaderProvider();
                }
            }
        }
        return mProvider;
    }

}

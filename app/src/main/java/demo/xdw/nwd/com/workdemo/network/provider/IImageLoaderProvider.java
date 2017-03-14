package demo.xdw.nwd.com.workdemo.network.provider;

import android.content.Context;

import demo.xdw.nwd.com.workdemo.network.ImageRequest;


public interface IImageLoaderProvider {

    void loadImage(ImageRequest request);

    void loadImage(Context context, ImageRequest request);
}

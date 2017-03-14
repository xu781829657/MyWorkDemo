package demo.xdw.nwd.com.workdemo.network.provider;

import android.content.Context;

import com.android.base.frame.Base;
import com.squareup.picasso.Picasso;

import demo.xdw.nwd.com.workdemo.network.ImageRequest;


public class PicassoImageLoaderProvider implements IImageLoaderProvider {
    @Override
    public void loadImage(ImageRequest request) {
        Picasso.with(Base.getContext()).load(request.getUrl()).placeholder(request.getPlaceHolder()).into(request.getImageView());
    }

    @Override
    public void loadImage(Context context, ImageRequest request) {
        Picasso.with(context).load(request.getUrl()).placeholder(request.getPlaceHolder()).into(request.getImageView());
    }
}

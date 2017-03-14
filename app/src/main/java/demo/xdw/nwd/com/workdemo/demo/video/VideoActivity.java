package demo.xdw.nwd.com.workdemo.demo.video;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.base.frame.Base;
import com.android.base.frame.activity.BaseActivity;
import com.android.base.util.ScreenUtils;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xudengwang on 2017/1/16.
 */

public class VideoActivity extends BaseActivity {

    @Bind(R.id.iv_video_thumb)
    ImageView mIvVideoThumb;
    //http://7xrr3u.com1.z0.glb.clouddn.com/001%20Welcome.mp4
    // private static final String VIDEO_URL = "http://7xrr3u.com1.z0.glb.clouddn.com/001%20Welcome.mp4";

    private static final String VIDEO_URL = "/storage/sdcard0/abc.mp4";

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_tip)
    TextView mTvTip;
    @Bind(R.id.tv_money)
    TextView mTvMoney;

    private static final String TITLE = "借款标题借款标题借款标题借款标题借款标题";
    private static final String TIPS = "提示";
    private static final String MONEY = "1000万";

    @Override
    protected void initData() {
        mContext = this;
        //createVideoThumbnail2(VIDEO_URL);

        mTvTip.setText(TIPS);
        mTvMoney.setText(MONEY);
        int money_width = mTvMoney.getMeasuredWidth();
        int tip_width = mTvTip.getMeasuredWidth();
        LogUtils.d(getClass(),"money_width:"+money_width);
        LogUtils.d(getClass(),"tip_width:"+tip_width);

        mTvTitle.setMaxWidth(ScreenUtils.getScreenWidth(mContext)- (int)(45*ScreenUtils.getScreenDensity(mContext))- mTvMoney.getWidth()- mTvTip.getWidth());
        mTvTitle.setText(TITLE);
        LogUtils.d(getClass(),"sdcard path:"+Environment.getExternalStorageDirectory());
        MediaDecoder decoder = new MediaDecoder(VIDEO_URL);
        decoder.decodeFrame(2000, new MediaDecoder.OnGetBitmapListener() {
            @Override
            public void getBitmap(Bitmap bitmap, long timeMs) {
                LogUtils.d(getClass(),"OnGetBitmapListener bitmap :"+bitmap);
                if (bitmap != null) {
                    mIvVideoThumb.setImageBitmap(bitmap);
                }

            }
        });

//        rx.Observable.just(createVideoThumbnail(VIDEO_URL,400,300))
//                .subscribeOn(Schedulers.io())
//
//                .subscribe(new Subscriber<Bitmap>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        Base.showToast("" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(Bitmap bitmap) {
//                        if (bitmap != null) {
//                            mIvVideoThumb.setImageDrawable(new BitmapDrawable(bitmap));
//                        }
//                    }
//                });

    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_video;
    }



    private Bitmap createVideoThumbnail2(String url) {
        File file = new File(url);
        LogUtils.d(getClass(), "url:" + url + ",file.isexit:" + file.exists());
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            retriever.setDataSource(file.getAbsolutePath());
            bitmap = retriever.getFrameAtTime();
            if (bitmap != null) {
                mIvVideoThumb.setImageBitmap(bitmap);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        return bitmap;
    }


//    private Bitmap createVideoThumbnail(String url, int width, int height) {
//        File file = new File(url);
//        LogUtils.d(getClass(), "url:" + url + ",file.isexit:" + file.exists());
//        Bitmap bitmap = null;
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
//        try {
//            if (Build.VERSION.SDK_INT >= 14) {
//                retriever.setDataSource(url, new HashMap<String, String>());
//            } else {
//                retriever.setDataSource(url);
//            }
//            bitmap = retriever.getFrameAtTime();
//            if (bitmap != null) {
//                mIvVideoThumb.setImageBitmap(bitmap);
//            }
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//            // Assume this is a corrupt video file
//        } catch (RuntimeException ex) {
//            ex.printStackTrace();
//            // Assume this is a corrupt video file.
//        } finally {
//            try {
//                retriever.release();
//            } catch (RuntimeException ex) {
//                // Ignore failures while cleaning up.
//            }
//        }
//        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        }
//        return bitmap;
//    }


    /**
     * 获取视频的缩略图
     * 提供了一个统一的接口用于从一个输入媒体文件中取得帧和元数据。
     * @param path 视频的路径
     * @param width 缩略图的宽
     * @param height 缩略图的高
     * @return 缩略图
     */
    public static Bitmap createVideoThumbnail(String path, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            retriever.setDataSource(path);
            bitmap = retriever.getFrameAtTime(5000); //取得指定时间的Bitmap，即可以实现抓图（缩略图）功能
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) {
            return null;
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return bitmap;
    }
}

package com.egale.glide;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

public class BitmapRequest {

    // 请求路径
    private String url;

    // 上下文
    private Context context;

    // 需要加载图片的控件
    private SoftReference<ImageView> imageView;

    // 占位图片
    private int resId;

    // 回调对象
    private RequestListener requestListener;

    // 图片的标识
    private String urlMd5;

    public BitmapRequest(Context context) {
        this.context = context;
    }

    // 链式调度
    // 加载url
    public BitmapRequest load(String url) {
        this.url = url;
        if (!TextUtils.isEmpty(url))
            this.urlMd5 = MD5.MD516(url);
        return this;
    }

    // 设置占位图片
    public BitmapRequest loadding(int resId) {
        this.resId = resId;
        return this;
    }

    // 设置监听器
    public BitmapRequest setListener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    // 显示图片的控件
    public void into(ImageView imageView) {
        imageView.setTag(urlMd5);
        this.imageView = new SoftReference<>(imageView);
        RequestManager.getInstance(context).addBitmapRequest(this);
    }

    public String getUrl() {
        return url;
    }

    public Context getContext() {
        return context;
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public int getResId() {
        return resId;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public String getUrlMd5() {
        return urlMd5;
    }
}

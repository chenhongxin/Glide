package com.egale.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

public class BitmapDispatcher extends Thread {

    Handler handler = new Handler(Looper.getMainLooper());

    // 创建一个阻塞线程
    private LinkedBlockingQueue<BitmapRequest> requestQueue;

    public BitmapDispatcher(LinkedBlockingQueue<BitmapRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        super.run();
        // 该线程没有被中断的时候
        while (!isInterrupted()) {
            if (requestQueue == null) {
                continue;
            }
            try {
                BitmapRequest bitmapRequest = requestQueue.take();
                if (bitmapRequest == null) {
                    continue;
                }
                // 设置占位图片
                showLoaddingImg(bitmapRequest);
                // 网络加载获取图片资源
                Bitmap bitmap = findBitmap(bitmapRequest);
                // 将图片显示到ImageView
                showImageView(bitmapRequest, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showImageView(final BitmapRequest bitmapRequest, final Bitmap bitmap) {
        final ImageView imageView = bitmapRequest.getImageView();
        if (bitmap != null && imageView != null && bitmapRequest.getUrlMd5().equals(imageView.getTag())) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                    RequestListener requestListener = bitmapRequest.getRequestListener();
                    if (requestListener != null) {
                        requestListener.onSuccess(bitmap);
                    }
                }
            });
        } else {
            RequestListener requestListener = bitmapRequest.getRequestListener();
            if (requestListener != null) {
                requestListener.onFaile();
            }
        }
    }

    private Bitmap findBitmap(BitmapRequest bitmapRequest) {
        // 这里需要通过三级缓存缓存图片
        Bitmap bitmap = null;
        bitmap = downloadBitmao(bitmapRequest.getUrl());
        return bitmap;
    }

    private void showLoaddingImg(BitmapRequest bitmapRequest) {
        final ImageView imageView = bitmapRequest.getImageView();
        if (bitmapRequest.getResId() > 0 && imageView != null) {
            final int resId = bitmapRequest.getResId();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(resId);
                }
            });
        }
    }

    private Bitmap downloadBitmao(String uri) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            is = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

}

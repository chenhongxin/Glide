package com.egale.glide;

import android.graphics.Bitmap;

public interface BitmapCache {

    void put(BitmapRequest bitmapRequest, Bitmap bitmap); // 存入内存

    Bitmap get(BitmapRequest bitmapRequest); // 读取内存中的图片

    void remove(BitmapRequest bitmapRequest); // 清楚缓存的图片

}

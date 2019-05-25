package com.egale.glide;

import android.content.Context;
import android.graphics.Bitmap;

public class DoubleLruCache implements BitmapCache {

    private MemberLruCache memberLruCache;
    private DiskBitmapCache diskBitmapCache;

    public DoubleLruCache(Context context) {
        memberLruCache = MemberLruCache.getInstance();
        diskBitmapCache = DiskBitmapCache.getInstance(context);
    }

    @Override
    public void put(BitmapRequest bitmapRequest, Bitmap bitmap) {
        memberLruCache.put(bitmapRequest, bitmap);
        diskBitmapCache.put(bitmapRequest, bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest bitmapRequest) {
        Bitmap bitmap = memberLruCache.get(bitmapRequest);
        if (bitmap == null) {
            bitmap = diskBitmapCache.get(bitmapRequest);
            if (bitmap != null) {
                memberLruCache.put(bitmapRequest, bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest bitmapRequest) {
        memberLruCache.remove(bitmapRequest);
        diskBitmapCache.remove(bitmapRequest);
    }
}

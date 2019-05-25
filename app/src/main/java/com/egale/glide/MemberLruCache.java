package com.egale.glide;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemberLruCache implements BitmapCache {

    private LruCache<String, Bitmap> lruCache;

    private static volatile MemberLruCache instance;

    private static final byte[] lock = new byte[0];

    public static MemberLruCache getInstance() {
        if (instance == null) {
            synchronized (MemberLruCache.class) {
                if (instance == null) {
                    instance = new MemberLruCache();
                }
            }
        }
        return instance;
    }

    private MemberLruCache() {
        int maxMemorySize = (int) (Runtime.getRuntime().maxMemory() / 16);
        if (maxMemorySize <= 0) {
            maxMemorySize = 10 * 1024 * 1024; // 开辟10M的缓存空间
        }
        lruCache = new LruCache<String, Bitmap>(maxMemorySize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

    }

    @Override
    public void put(BitmapRequest bitmapRequest, Bitmap bitmap) {
        if (bitmap != null) {
            lruCache.put(bitmapRequest.getUrlMd5(), bitmap);
        }
    }

    @Override
    public Bitmap get(BitmapRequest bitmapRequest) {
        return lruCache.get(bitmapRequest.getUrlMd5());
    }

    @Override
    public void remove(BitmapRequest bitmapRequest) {
        lruCache.remove(bitmapRequest.getUrlMd5());
    }
}

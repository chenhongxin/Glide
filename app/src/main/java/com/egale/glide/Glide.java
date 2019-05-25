package com.egale.glide;

import android.content.Context;

public class Glide {

    // 创建请求
    public static BitmapRequest with(Context context) {
        return new BitmapRequest(context);
    }

}

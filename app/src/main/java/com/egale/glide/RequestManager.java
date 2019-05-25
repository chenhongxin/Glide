package com.egale.glide;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

// 这个是所有请求分配的管理者，负责分配到哪个请求去处理
public class RequestManager {

    private static RequestManager requestManager;
    private LinkedBlockingQueue<BitmapRequest> requestQueue = new LinkedBlockingQueue<>();
    private BitmapDispatcher[] bitmapDispatchers; // 管理者需要分配多少个处理器来处理请求，而处理器的多少是由手机的内存线程数量来分配的
    /**
     * 线程池管理线程
     */
    public ExecutorService executorService;

    // 上下文
    private Context context;

    public static RequestManager getInstance(Context context) {
        if (requestManager == null) {
            synchronized (DiskBitmapCache.class) {
                if (requestManager == null) {
                    requestManager = new RequestManager(context);
                }
            }
        }
        return requestManager;
    }

    private RequestManager(Context context) {
        this.context = context;
        // 初始化线程池
        initThreadExecutor();
        // 只有一个管理者，所有在这里启动最合适
        start();
    }

    public void initThreadExecutor() {
        int size = Runtime.getRuntime().availableProcessors();
        if (size <= 0) {
            size = 1;
        }
        size *= 2;
        executorService = Executors.newFixedThreadPool(size);
    }

    public void start() {
        stop();
        startAllDispatcher();
    }

    // 这里收集所有请求
    public void addBitmapRequest(BitmapRequest bitmapRequest) {
        if (bitmapRequest == null) {
            return;
        }
        if (!requestQueue.contains(bitmapRequest)) {
            requestQueue.add(bitmapRequest); // 将请求加入队列
        }
    }

    // 处理并开始所有的线程
    public void startAllDispatcher() {
        // 获取线程最大数量
        final int threadCount = Runtime.getRuntime().availableProcessors();
        bitmapDispatchers = new BitmapDispatcher[threadCount];
        if (bitmapDispatchers.length > 0) {
            for (int i = 0; i < threadCount; i++) {
                // 线程数量开辟的请求分发去抢请求资源对象，谁抢到了，就由谁去处理
                BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue, context);
                executorService.execute(bitmapDispatcher);
                // 将每个dispatcher放到数组中，方便统一处理
                bitmapDispatchers[i] = bitmapDispatcher;
            }
        }
    }

    // 停止所有的线程
    public void stop() {
        if (bitmapDispatchers != null && bitmapDispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : bitmapDispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.interrupt(); // 中断
                }
            }
        }
    }

}

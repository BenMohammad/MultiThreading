package com.benmohammad.multithreading.common.di;

import android.util.Log;

import java.util.concurrent.SynchronousQueue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ApplicationCompositionRoot {

    private ThreadPoolExecutor mThreadPoolExecutor;

    public ThreadPoolExecutor getThreadPoolExecutor() {
        if(mThreadPoolExecutor == null) {
            mThreadPoolExecutor = new ThreadPoolExecutor(
                    10, Integer.MAX_VALUE, 10, TimeUnit.SECONDS,
                    new SynchronousQueue<>(),
                    r -> {
                        Log.d("ThreadFactory", String.format(
                                "size $s", "active count $s", "queue remaining",
                                mThreadPoolExecutor.getPoolSize(),
                                mThreadPoolExecutor.getActiveCount(),
                                mThreadPoolExecutor.getQueue().remainingCapacity()));

                        return new Thread(r);
                    }
            );
        }

        return mThreadPoolExecutor;
    }
}

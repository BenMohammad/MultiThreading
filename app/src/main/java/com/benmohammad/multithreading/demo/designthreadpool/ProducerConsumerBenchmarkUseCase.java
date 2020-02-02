package com.benmohammad.multithreading.demo.designthreadpool;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.benmohammad.multithreading.common.BaseObservable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumerBenchmarkUseCase extends BaseObservable<ProducerConsumerBenchmarkUseCase.Listener> {

    public static interface Listener {
        void onBenchmarkCompleted(Result result);
    }

    public static class Result {
        private final long mExecutionTime;
        private final int mNumOfReceivedMessages;

        public Result(long executionTime, int numOfReceivedMessages) {
            this.mExecutionTime = executionTime;
            this.mNumOfReceivedMessages = numOfReceivedMessages;
        }

        public long getExecutionTime() {
            return mExecutionTime;
        }

        public int getNumOfReceivedMessages() {
            return mNumOfReceivedMessages;
        }
    }

    public static final int NUM_OF_MESSAGES = 1000;
    public static final int BLOCKING_QUEUE_CAPACITY = 5;

    private final Object LOCK = new Object();
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private final AtomicInteger mNumOfThreads = new AtomicInteger(0);
    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);

    private final ExecutorService mThreadPool = Executors.newCachedThreadPool(
            r -> {
                Log.d("ThreadFactory", "thread: " + mNumOfThreads.incrementAndGet());
                return new Thread(r);
            }
    );

    private int mNumOfFinishedThreads;
    private int mNumOfReceivedMessages;
    private long mStartTimestamp;

    public void startBenchmarkAndNotify() {
        synchronized (LOCK) {
            mNumOfReceivedMessages = 0;
            mNumOfFinishedThreads = 0;
            mStartTimestamp = System.currentTimeMillis();
            mNumOfThreads.set(0);
        }

        mThreadPool.execute(() -> {
            synchronized (LOCK) {
                while(mNumOfFinishedThreads < NUM_OF_MESSAGES) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e)  {
                        return;
                    }
                }
            }

            notifySuccess();

        });

        mThreadPool.execute(() -> {
            for(int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewProducer(i);
            }
        });

        mThreadPool.execute(() -> {
            for(int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewConsumer();
            }
        });

    }


    private void startNewProducer(final int index) {
        mThreadPool.execute(() -> mBlockingQueue.put(index));
    }

    private void startNewConsumer() {
        mThreadPool.execute(() -> {
            int message = mBlockingQueue.take();
            synchronized (LOCK) {
                if(message != -1) {
                    mNumOfReceivedMessages++;
                }
                mNumOfFinishedThreads++;
                LOCK.notifyAll();
            }
        });
    }

    private void notifySuccess() {
        mUiHandler.post(() -> {
            Result result;
            synchronized (LOCK) {
                result = new Result(
                        System.currentTimeMillis() - mStartTimestamp,
                        mNumOfReceivedMessages
                );
                for(Listener listener : getListener()) {
                    listener.onBenchmarkCompleted(result);
                }
            }
        });
    }

}

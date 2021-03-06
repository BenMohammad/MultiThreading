package com.benmohammad.multithreading.demo.designthread;

import android.os.Handler;
import android.os.Looper;

import com.benmohammad.multithreading.common.BaseObservable;

public class ProducerConsumerBenchmarkUseCase extends BaseObservable<ProducerConsumerBenchmarkUseCase.Listener> {

    public interface Listener {
        void onBenchmarkCompleted(Result result);
    }

    public static class Result {
        private final long mExecutionTime;
        private final long mNumOfReceivedMessages;

        public Result(long executionTime, long numOfReceivedMessages) {
            this.mExecutionTime = executionTime;
            this.mNumOfReceivedMessages = numOfReceivedMessages;
        }

        public long getExecutionTime() {
            return mExecutionTime;
        }

        public long getNumOfReceivedMessages() {
            return mNumOfReceivedMessages;
        }
    }

    private static final int NUM_OF_MESSAGES = 1000;
    private static final int BLOCKING_QUEUE_CAPACITY = 5;
    private final Object LOCK = new Object();
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private int mNumOfFinishedConsumers;
    private int mNumOfReceivedMessages;
    private long mStartTimestamp;

    public void startBenchmarkAndNotify() {
        synchronized (LOCK) {
            mNumOfReceivedMessages = 0;
            mNumOfFinishedConsumers = 0;
            mStartTimestamp = System.currentTimeMillis();
        }

        new Thread(() -> {
            synchronized (LOCK)  {
                while(mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

            }

            notifySuccess();
        }).start();


        new Thread(() -> {
            for(int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewProducer(i);
            }
        }).start();

        new Thread(() -> {
            for(int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewConsumer();
            }
        }).start();

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

    private void startNewConsumer() {
        new Thread(() -> {
            int message = mBlockingQueue.take();
            synchronized (LOCK) {
                if(message != -1) {
                    mNumOfReceivedMessages++;
                }

                mNumOfFinishedConsumers++;
                LOCK.notifyAll();

            }
        }).start();
    }

    private void startNewProducer(final int index) {
        new Thread(() -> mBlockingQueue.put(index)).start();
    }


    @Override
    protected void onFirstListenerRegistered() {

    }

    @Override
    protected void onLastListenerRegistered() {

    }
}

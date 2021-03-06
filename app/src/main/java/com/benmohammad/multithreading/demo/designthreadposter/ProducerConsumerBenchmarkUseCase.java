package com.benmohammad.multithreading.demo.designthreadposter;

import com.benmohammad.multithreading.common.BaseObservable;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

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

    private static final int NUM_OF_MESSAGES = 1000;
    private static final int BLOCKING_QUEUE_CAPACITY = 5;

    private final Object LOCK = new Object();
    private final UiThreadPoster mUiThreadPoster = new UiThreadPoster();
    private final BackgroundThreadPoster mBackgroundThreadPoster = new BackgroundThreadPoster();
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

        mBackgroundThreadPoster.post(() -> {
            synchronized (LOCK) {
                while(mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }

            notifySuccess();

        });


        mBackgroundThreadPoster.post(() -> {
            for(int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewProducer(i);
            }
        });

        mBackgroundThreadPoster.post(() -> {
            for(int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewConsumer();
            }
        });

    }


    private void notifySuccess() {
        mUiThreadPoster.post(() -> {
            Result result;
            synchronized (LOCK) {
                result = new Result(
                        System.currentTimeMillis() - mStartTimestamp,
                        mNumOfReceivedMessages
                );

            }

            for(Listener listener : getListener()) {
                listener.onBenchmarkCompleted(result);
            }
        });
    }

    private void startNewProducer(final int index) {
        mBackgroundThreadPoster.post(() -> mBlockingQueue.put(index));
    }

    private void startNewConsumer() {
        mBackgroundThreadPoster.post(() -> {
            int message = mBlockingQueue.take();
            synchronized (LOCK) {
                if(message != -1) {
                    mNumOfReceivedMessages++;
                }

                mNumOfFinishedConsumers++;
                LOCK.notifyAll();
            }
        });
    }


}

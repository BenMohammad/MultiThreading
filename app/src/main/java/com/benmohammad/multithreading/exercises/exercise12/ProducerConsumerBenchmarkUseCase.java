package com.benmohammad.multithreading.exercises.exercise12;

import com.benmohammad.multithreading.common.BaseObservable;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

public class ProducerConsumerBenchmarkUseCase extends BaseObservable<ProducerConsumerBenchmarkUseCase.Listener> {

    public interface Listener {
        void onBenchmarkCompleted(Result result);
    }

    public static class Result {
        private final long mExecutionTime;
        private final int mNumOfReceivedMessages;

        public Result(long executionTime, int numOfReceivedMessages) {
            this.mExecutionTime = executionTime;
            this.mNumOfReceivedMessages = numOfReceivedMessages;
        }

        public int getNumOfReceivedMessages() {
            return mNumOfReceivedMessages;
        }

        public long getExecutionTime() {
            return mExecutionTime;
        }
    }

    private static  final int NUM_OF_MESSAGES = 1000;
    private static  final int BLOCKING_QUEUE_CAPACITY = 5;
    private final Object LOCK = new Object();
    private final UiThreadPoster mUiTHreadPoster = new UiThreadPoster();
    private final BackgroundThreadPoster mBackgroundThreadPoster = new BackgroundThreadPoster();
    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private int mNumOfFinishedConsumers;
    private int mNumOfReceivedMessages;

    public void startBenchmarkAndNotify() {
        mBackgroundThreadPoster.post(() -> {
            mNumOfReceivedMessages = 0;
            mNumOfFinishedConsumers = 0;

            long startTimeStamp = System.currentTimeMillis();


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

            waitForAllConsumersToFinish();

            Result result;
            synchronized (LOCK) {
                result = new Result(
                        System.currentTimeMillis() - startTimeStamp,
                        mNumOfReceivedMessages
                );

                notifySuccess(result);
            }
        });
    }



    private void waitForAllConsumersToFinish() {
        synchronized (LOCK) {
            while(mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                try {
                    LOCK.wait();
                } catch(InterruptedException e) {
                    return;
                }
            }
        }
    }


    private void startNewProducer(int index) {
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

    private void notifySuccess(Result result) {
        mUiTHreadPoster.post(() -> {
            for(Listener listener : getListener()) {
                listener.onBenchmarkCompleted(result);
            }
        });
    }

}

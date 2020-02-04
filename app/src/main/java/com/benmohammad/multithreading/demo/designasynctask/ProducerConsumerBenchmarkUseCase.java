package com.benmohammad.multithreading.demo.designasynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.benmohammad.multithreading.common.BaseObservable;

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
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private final MyBlockingQueue mBlockinQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private int mNumOfFinishedConsumers;
    private int mNumOfReceivedMessages;

    private long startTimestamp;

    public void startBenchmarkAndNotify() {
        synchronized (LOCK) {
            mNumOfReceivedMessages = 0;
            mNumOfFinishedConsumers = 0;
            startTimestamp = System.currentTimeMillis();
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                synchronized (LOCK) {
                    while(mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            return null;
                        }
                    }
                }

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifySuccess();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
        Result result;
        synchronized (LOCK) {
            result = new Result(
                    System.currentTimeMillis() - startTimestamp,
                    mNumOfReceivedMessages
            );
        }
        for(Listener listener : getListener()) {
            listener.onBenchmarkCompleted(result);
        }
    }

    private void startNewProducer(final int index) {
        new Thread(() -> {
            mBlockinQueue.put(index);
        }).start();
    }

    private void startNewConsumer() {
        new Thread(() -> {
            int message = mBlockinQueue.take();
            synchronized (LOCK) {
                if(message != -1) {
                    mNumOfReceivedMessages++;
                }
                mNumOfFinishedConsumers++;
                LOCK.notifyAll();
            }


        }).start();
    }

}

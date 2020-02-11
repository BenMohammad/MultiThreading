package com.benmohammad.multithreading.exercises.exercise7;

import android.os.Handler;

import androidx.annotation.WorkerThread;

import com.benmohammad.multithreading.common.BaseObservable;

import java.math.BigInteger;
import java.util.concurrent.ThreadPoolExecutor;

public class ComputeFactorialUseCase extends BaseObservable<ComputeFactorialUseCase.Listener> {

    public interface Listener {
        void onFactorialComputed(BigInteger result);
        void onFactorialTimeout();
        void onFactorialAborted();
    }

    private final Object LOCK = new Object();
    private final Handler mUiHandler;
    private final ThreadPoolExecutor mThreadPoolExecutor;
    private int mNumOfThreads = 0;
    private ComputationRange[] mThreadComputationRanges;
    private volatile BigInteger[] mThreadComputationResults;
    private int mNumOfFinishedThreads;
    private long mComputationTimeout;
    private boolean mAbortComputation;


    public ComputeFactorialUseCase(Handler uiHandler, ThreadPoolExecutor threadPoolExecutor) {
        this.mUiHandler = uiHandler;
        this.mThreadPoolExecutor = threadPoolExecutor;
    }

    @Override
    protected void onLastListenerRegistered() {
        super.onLastListenerRegistered();
        synchronized (LOCK) {
            mAbortComputation = true;
            LOCK.notifyAll();
        }
    }

    public void computeFactorialAndNotify(final int argument, final int timeout) {
        mThreadPoolExecutor.execute(() -> {
            initComputationParams(argument, timeout);
            startComputation();
            waitForThreadsResultsOrTimeoutOrAbort();
            processComputationResult();
        });
    }
    @WorkerThread
    private void processComputationResult() {
        if(mAbortComputation) {
            notifyAbort();
            return;
        }

        BigInteger result = computeFinalResult();

        if(isTimeout()) {
            notifyTimeout();
            return;
        }

        notifySuccess(result);
    }

    private void notifySuccess(BigInteger result) {
        mUiHandler.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialComputed(result);
            }
        });
    }

    @WorkerThread
    private BigInteger computeFinalResult() {
        BigInteger result = new BigInteger("1");
        for(int i = 0; i < mNumOfThreads; i++) {
            if(isTimeout()) {
                break;
            }
            result = result.multiply(mThreadComputationResults[i]);
        }

        return result;

    }

    private void notifyTimeout() {
        mUiHandler.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialTimeout();
            }
        });
    }

    private void notifyAbort() {
        mUiHandler.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialAborted();
            }
        });
    }

    private void waitForThreadsResultsOrTimeoutOrAbort() {
        synchronized (LOCK) {
            while(mNumOfFinishedThreads != mNumOfThreads && !isTimeout() && !mAbortComputation) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

        }
    }

    @WorkerThread
    private void startComputation() {
        for(int i = 0; i < mNumOfThreads; i++) {
            final int threadIndex = i;

            new Thread(() -> {
                long rangeStart = mThreadComputationRanges[threadIndex].start;
                long rangeEnd = mThreadComputationRanges[threadIndex].end;

                BigInteger product = new BigInteger("1");
                for(long num = rangeStart; num <= rangeEnd; num++) {
                    if(isTimeout()) {
                        break;
                    }
                    product = product.multiply(new BigInteger(String.valueOf(num)));
                }
                mThreadComputationResults[threadIndex] = product;

                synchronized (LOCK) {
                    mNumOfFinishedThreads++;
                    LOCK.notifyAll();
                }

            }).start();
        }
    }

    private boolean isTimeout() {
        return System.currentTimeMillis() >= mComputationTimeout;
    }

    private void initComputationParams(int argument, int timeout) {
        mNumOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();
        synchronized (LOCK) {
            mNumOfFinishedThreads = 0;
            mAbortComputation = false;
        }

        mThreadComputationResults = new BigInteger[mNumOfThreads];
        mThreadComputationRanges = new ComputationRange[mNumOfThreads];
        initThreadsComputationRanges(argument);
        mComputationTimeout = System.currentTimeMillis() + timeout;
    }

    private void initThreadsComputationRanges(int argument) {
        int computationRangeSize = argument / mNumOfThreads;

        long nextComputationRangeEnd = argument;
        for(int i = mNumOfThreads - 1; i >= 0; i--) {
            mThreadComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );

            nextComputationRangeEnd = mThreadComputationRanges[i].start;

        }

        mThreadComputationRanges[0].start = 1;

    }


    private static class ComputationRange {
        private long start;
        private long end;

        public ComputationRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }


}

package com.benmohammad.multithreading.exercises.exercise8;

import com.benmohammad.multithreading.common.BaseObservable;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.math.BigInteger;

public class ComputeFactorialUseCase extends BaseObservable<ComputeFactorialUseCase.Listener> {

    public interface Listener {
        void onFactorialComputed(BigInteger result);
        void onFactorialTimedOut();
        void onFactorialAborted();
    }

    private final Object LOCK = new Object();
    private final  UiThreadPoster mUiThreadPoster;
    private final BackgroundThreadPoster mBackgroundThreadPoster;
    private int mNumberOfThreads;
    private CompuationRange[] mThreadsComputationRanges;
    private volatile BigInteger[] mThreadsComputationResults;
    private int mNumOfFinishedThreads = 0;
    private long mComputationTimeout;
    private boolean mAbortComputation;

    public ComputeFactorialUseCase(UiThreadPoster uiThreadPoster, BackgroundThreadPoster backgroundThreadPoster) {
        this.mUiThreadPoster = uiThreadPoster;
        this.mBackgroundThreadPoster = backgroundThreadPoster;
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
        mBackgroundThreadPoster.post(() -> {
            initComputationParams(argument, timeout);
            startComputation();
            waitForThreadOrTimeoutOrAbort();
            processComputationResult();
        });
    }

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

    private BigInteger computeFinalResult() {
        BigInteger result = new BigInteger("1");
        for(int i = 0; i < mNumberOfThreads; i++) {
            if(isTimeout()) {
                break;
            }
            result = result.multiply(mThreadsComputationResults[i]);
        }

        return result;
    }

    private void notifySuccess(BigInteger result) {
        mUiThreadPoster.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialComputed(result);
            }
        });
    }

    private void notifyTimeout() {
        mUiThreadPoster.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialTimedOut();
            }
        });
    }

    private void notifyAbort() {
        mUiThreadPoster.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialAborted();
            }
        });
    }

    private void waitForThreadOrTimeoutOrAbort() {
        synchronized (LOCK) {
            while(mNumOfFinishedThreads != mNumberOfThreads && !isTimeout() && ! mAbortComputation) {
                try {
                    LOCK.wait();
                } catch(InterruptedException e) {
                    return;
                }
            }
        }
    }

    private void startComputation() {
        for(int i = 0; i < mNumberOfThreads; i++) {
            int threadIndex = i;
            mUiThreadPoster.post(() -> {
                long rangeStart = mThreadsComputationRanges[threadIndex].start;
                long rangeEnd = mThreadsComputationRanges[threadIndex].end;

                BigInteger product = new BigInteger("1");
                for(long num = rangeStart; num <= rangeEnd; num++) {
                    if(isTimeout()) {
                        break;
                    }
                    product = product.multiply(new BigInteger(String.valueOf(num)));
                }
                mThreadsComputationResults[threadIndex] = product;
                synchronized (LOCK) {
                    mNumOfFinishedThreads++;
                    LOCK.notifyAll();
                }
            });
        }
    }

    private boolean isTimeout() {
        return System.currentTimeMillis()  >= mComputationTimeout;
    }

    private void initComputationParams(int argument, int timeout) {
        mNumberOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();

        synchronized (LOCK) {
            mNumOfFinishedThreads = 0;
            mAbortComputation = false;
        }

        mThreadsComputationRanges = new CompuationRange[mNumberOfThreads];
        mThreadsComputationResults = new BigInteger[mNumberOfThreads];
        initThreadComputationRanges(argument);
        mComputationTimeout = System.currentTimeMillis() + timeout;
    }

    private void initThreadComputationRanges(int argument) {
        int computationRangeSize = argument / mNumberOfThreads;
        long nextComputationRangeEnd = argument;
        for(int i = mNumberOfThreads - 1; i >= 0; i--) {
            mThreadsComputationRanges[i] = new CompuationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );
            nextComputationRangeEnd = mThreadsComputationRanges[i].start - 1;
        }
        mThreadsComputationRanges[0].start = 1;
    }


    private static class CompuationRange {
        private long start;
        private long end;

        public CompuationRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

}

package com.benmohammad.multithreading.exercises.exercise9;

import java.math.BigInteger;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ComputeFactorialUseCase {

    public static class Result {
        private final boolean mIsAborted;
        private final boolean mIsTimeout;
        private final BigInteger mResult;

        public Result(boolean isAborted, boolean isTimeout, BigInteger result) {
            this.mIsAborted = isAborted;
            this.mIsTimeout = isTimeout;
            this.mResult = result;
        }

        public boolean isAborted() {
            return mIsAborted;
        }

        public boolean isTimeout() {
            return mIsTimeout;
        }

        public BigInteger getResult() {
            return mResult;
        }
    }

    private int mNumOfThreads;
    private ComputationRange[] mThreadsComputationRanges;
    private long mComputationTimeout;

    public Observable<Result> computeFactorial(final int argument, final int timeout) {
        initComputationParams(argument, timeout);
        return Flowable
                .range(0, mNumOfThreads)
                .parallel(mNumOfThreads)
                .runOn(Schedulers.io())
                .map(this::computePart)
                .sequential()
                .scan((bigInteger, bigInteger2) -> {
                    if(isTimeout()) {
                        return bigInteger;
                    } else {
                        return bigInteger.multiply(bigInteger2);
                    }

                }).last(new BigInteger("0"))
                .map(result -> {
                    if(isTimeout()) {
                        return new Result(false, false, result);
                    } else {
                        return new Result(false, false, result);
                    }
                }).onErrorReturnItem(new Result(true, false, new BigInteger("0")))
                .toObservable();

    }

    private BigInteger computePart(Integer id) {
        long rangeStart = mThreadsComputationRanges[id].start;
        long rangeEnd = mThreadsComputationRanges[id].end;

        BigInteger product = new BigInteger("1");
        for(long num = rangeStart; num <= rangeEnd; num++) {
            if(isTimeout()) {
                break;
            }
            product = product.multiply(new BigInteger(String.valueOf(num)));
        }
        return product;

    }

    private boolean isTimeout() {
        return System.currentTimeMillis() >= mComputationTimeout;
    }

    private void initComputationParams(int argument, int timeout) {
        mNumOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();
        mThreadsComputationRanges = new ComputationRange[mNumOfThreads];
        initThreadsComputationRange(argument);
        mComputationTimeout = System.currentTimeMillis() + timeout;
    }

    private void initThreadsComputationRange(int argument) {
        int computationRangeSize = argument / mNumOfThreads;
        long nextComputationRangeEnd = argument;
        for(int i = mNumOfThreads - 1; i >= 0; i--) {
            mThreadsComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );
            nextComputationRangeEnd = mThreadsComputationRanges[i].start -1;
        }
        mThreadsComputationRanges[0].start = 1;
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

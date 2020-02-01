package com.benmohammad.multithreading.demo.designthread;

import java.util.LinkedList;
import java.util.Queue;

class MyBlockingQueue {

    private final Object QUEUE_LOCK = new Object();
    private final int mCapacity;
    private int mCurrentSize = 0;
    private final Queue<Integer> mQueue = new LinkedList<>();

    public MyBlockingQueue(int capacity) {
        this.mCapacity = capacity;
    }

    public void put(int number) {
        synchronized (QUEUE_LOCK) {
            while(mCurrentSize >= mCapacity) {
                try {
                    QUEUE_LOCK.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
            mQueue.offer(number);
            mCurrentSize++;
            QUEUE_LOCK.notifyAll();
        }
    }

    public int take() {
        synchronized (QUEUE_LOCK) {
            while(mCurrentSize <= 0) {
                try {
                    QUEUE_LOCK.wait();
                } catch (InterruptedException e) {
                    return 0;
                }
            }

            mCurrentSize--;
            QUEUE_LOCK.notifyAll();
            return mQueue.poll();

        }
    }

}

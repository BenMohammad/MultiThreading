package com.benmohammad.multithreading.demo.designcoroutines

import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

internal class MyBlockingQueue(private val capacity : Int) {

    private val reentrantLock = ReentrantLock()
    private val lockCondition = reentrantLock.newCondition()
    private val queue = LinkedList<Int>()
    private var currentSize = 0

    fun put(number : Int) {
        reentrantLock.withLock {
            while(currentSize >= capacity) {
                try {
                    lockCondition.await()
                } catch (e: InterruptedException) {
                    return
                }
            }
            queue.offer(number)
            currentSize++
            lockCondition.signalAll()
        }
    }

    fun take(): Int {
        reentrantLock.withLock {
            while (currentSize <= 0) {
                try {
                    lockCondition.await()
                } catch (e: InterruptedException) {
                    return 0
                }
            }
            currentSize--
            lockCondition.signalAll()
            return queue.poll()
        }
    }


}
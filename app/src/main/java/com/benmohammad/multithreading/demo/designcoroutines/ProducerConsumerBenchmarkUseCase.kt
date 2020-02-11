package com.benmohammad.multithreading.demo.designcoroutines

import android.os.Handler
import android.os.Looper
import com.benmohammad.multithreading.common.BaseObservable
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ProducerConsumerBenchmarkUseCase : BaseObservable<ProducerConsumerBenchmarkUseCase.Listener>() {

    interface Listener {
        fun onBenchmarkCompleted(result: Result)
    }

    class Result(val executionTime : Long, val numOfreceivedMessages : Int)

    private val reentrantLock = ReentrantLock()
    private val lockCondition = reentrantLock.newCondition()

    private val uiHandler = Handler(Looper.getMainLooper())
    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)
    private var numOfFinishedConsumers : Int = 0
    private var numOfReceivedMessages : Int = 0
    private var startTimestamp : Long = 0

    fun startBenchmarkAndNotify() {
        reentrantLock.withLock {
            numOfReceivedMessages = 0
            numOfFinishedConsumers = 0
            startTimestamp = System.currentTimeMillis()
        }


        Thread {
            reentrantLock.withLock {
                while(numOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        lockCondition.await()
                    } catch (e: InterruptedException) {
                        return@Thread
                    }
                }
            }
         notifySuccess()

    }.start()

        Thread{
            for(i in 0 until NUM_OF_MESSAGES) {
                startNewProducer(i)
            }
        }.start()

        Thread{
            for(i in 0 until NUM_OF_MESSAGES) {
                startNewConsumer()
            }
        }.start()

    }


    fun startNewProducer(index: Int) {
      Thread{
          blockingQueue.put(index)
      }.start()
    }

    fun startNewConsumer() {
        Thread {
            val message = blockingQueue.take()
            reentrantLock.withLock {
                if(message !== -1) {
                    numOfReceivedMessages++
                }
                numOfFinishedConsumers++
                lockCondition.signalAll()
            }
        }.start()
    }

    private fun notifySuccess() {
        uiHandler.post {
            lateinit var result : Result
            reentrantLock.withLock {
                result = Result(
                        System.currentTimeMillis() - startTimestamp,
                        numOfReceivedMessages
                )
            }

            for(listener in listener) {
                listener.onBenchmarkCompleted(result)
            }



        }
    }



    companion object {
        private const val NUM_OF_MESSAGES = 1000
        private const val BLOCKING_QUEUE_CAPACITY = 5
    }
}
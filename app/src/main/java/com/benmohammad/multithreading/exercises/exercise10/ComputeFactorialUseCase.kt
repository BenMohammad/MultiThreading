package com.benmohammad.multithreading.exercises.exercise10

import kotlinx.coroutines.*
import java.math.BigInteger

class ComputeFactorialUseCase {

    public sealed class Result {
        class Success(val result: BigInteger) : Result()
        object Timeout: Result()
    }

    suspend fun computeFactorial(argument: Int, timeout: Int) : Result {
        return withContext(Dispatchers.IO) {
            try {
                withTimeout(timeMillis = timeout.toLong()) {
                    val computationalRange = getComputationalRanges(argument)
                    val partialProductForRanges = computePartialProduct(computationalRange)
                    val result = computeFinalResult(partialProductForRanges)

                    Result.Success(result)
                }
            } catch (e: TimeoutCancellationException) {
                Result.Timeout
            }
        }
    }

    private fun getComputationalRanges(argument: Int) : Array<ComputationRange> {
        val numberOfThreads  = getNumberOfThreads(argument)
        val threadComputationRanges = Array(numberOfThreads) {
            ComputationRange(0,0)
        }
        val computationalRangeSize = argument / numberOfThreads
        var nextComputationalRangeEnd = argument.toLong()
        for(i in numberOfThreads  - 1 downTo 0) {
            threadComputationRanges[i] = ComputationRange(
                    nextComputationalRangeEnd - computationalRangeSize  +1,
                    nextComputationalRangeEnd
            )
            nextComputationalRangeEnd = threadComputationRanges[i].start - 1
        }
        threadComputationRanges[0] = ComputationRange(1, threadComputationRanges[0].end)

        return threadComputationRanges
    }

    private fun getNumberOfThreads(argument: Int) :Int {
        return if(argument< 20) 1 else Runtime.getRuntime().availableProcessors()
    }

    private suspend fun computePartialProduct(computationRange: Array<ComputationRange>) : List<BigInteger> = coroutineScope{
        return@coroutineScope computationRange.map {
            computeProductsForRangeAsync(it)
        }.map{
            it.await()
        }
    }

    private fun CoroutineScope.computeProductsForRangeAsync(computationRange : ComputationRange) : Deferred<BigInteger> = async(Dispatchers.IO) {
        val rangeStart = computationRange.start
        val rangeEnd = computationRange.end

        var product = BigInteger("1")
        for(num in rangeStart..rangeEnd) {
            if(!isActive) {
                break
            }
            product = product.multiply(BigInteger(num.toString()))
        }
        return@async product
    }

    private suspend fun computeFinalResult(partialProducts : List<BigInteger>) : BigInteger = withContext(Dispatchers.IO) {
        var result = BigInteger("1")
        for(partialProduct in partialProducts) {
            if(!isActive) {
                break
            }
            result = result.multiply(partialProduct)
        }
        return@withContext result
    }




    private data class ComputationRange(val start: Long, val end: Long)
}
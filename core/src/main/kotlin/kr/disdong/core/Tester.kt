package kr.disdong.core

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Tester {

    /**
     * poolSize 만큼 동시에 fn 메서드를 count 번 호출합니다.
     * @param fn
     */
    fun request(poolSize: Int, count: Int, fn: () -> Unit) {
        val es = Executors.newFixedThreadPool(poolSize)
        val latch = CountDownLatch(count)
        for (i in 1..count) {
            try {
                // logger.info("current count: ${latch.count}")
                es.submit {
                    fn()
                }
            } finally {
                latch.countDown()
            }
        }

        latch.await()
        es.awaitTermination(50, TimeUnit.MILLISECONDS)
        es.shutdown()
    }
}

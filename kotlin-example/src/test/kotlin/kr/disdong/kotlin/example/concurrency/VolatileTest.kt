package kr.disdong.kotlin.example.concurrency

import kr.disdong.core.Clogger
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class VolatileTest {

    private val logger = Clogger<VolatileTest>()

    // https://ttl-blog.tistory.com/238
    @Nested
    inner class FailCase {
        @Test
        @Disabled
        fun `두번째 스레드는 자신의 CPU cache 의 running 값을 false 로 수정하므로 첫번째 스레드는 종료되지 않는다`() {
            var running = true
            val thread1 = Thread {
                var count = 0
                while (running) {
                    count++
                }
                logger.info("Thread 1 finished. Counted up to $count")
            }
            thread1.start()
            Thread {
                Thread.sleep(100)
                logger.info("Thread 2 finishing")
                running = false
            }.start()

            // 종료되지 않습니다.
            while (thread1.isAlive) { }

            assertFalse(running)
        }
    }

    @Nested
    inner class SuccessCase {
        @Volatile
        private var running = true

        @Test
        fun `Volatile 로 선언된 변수는 cache 를 무시하므로 정상적으로 종료됩니다`() {
            val thread1 = Thread {
                var count = 0
                while (running) {
                    count++
                }
                logger.info("Thread 1 finished. Counted up to $count")
            }
            thread1.start()
            Thread {
                Thread.sleep(100)
                logger.info("Thread 2 finishing")
                running = false
            }.start()

            while (thread1.isAlive) { }

            assertFalse(thread1.isAlive)
            assertFalse(running)
        }
    }
}

package kr.disdong.kotlin.example.concurrency

import java.util.concurrent.atomic.AtomicInteger

class OptimisticLockNumber(
    private val value: Int = 0
) {

    private val atomicValue = AtomicInteger(value)

    fun plusOne() {
        atomicValue.addAndGet(1)
    }

    fun getValue(): Int {
        return atomicValue.get()
    }
}

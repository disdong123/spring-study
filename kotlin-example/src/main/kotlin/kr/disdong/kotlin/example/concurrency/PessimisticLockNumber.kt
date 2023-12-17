package kr.disdong.kotlin.example.concurrency

class PessimisticLockNumber(
    @Volatile
    var value: Int = 0
) {

    @Synchronized
    fun plusOne() {
        ++value
    }
}

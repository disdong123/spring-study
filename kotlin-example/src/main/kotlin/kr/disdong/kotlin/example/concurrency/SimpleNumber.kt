package kr.disdong.kotlin.example.concurrency

class SimpleNumber(
    var value: Int = 0
) {

    fun plusOne() {
        ++value
    }
}

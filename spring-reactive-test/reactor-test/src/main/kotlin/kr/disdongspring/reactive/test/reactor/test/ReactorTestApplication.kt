package kr.disdongspring.reactive.test.reactor.test

import kr.disdongspring.reactive.test.reactor.test.observer.test
import kr.disdongspring.reactive.test.reactor.test.observer.test2

class ReactorTestApplication

fun main() {
    println("${Thread.currentThread().name} start...")
    test2()
    println("${Thread.currentThread().name} end...")
}

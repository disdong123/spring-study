package kr.disdong.spring.reactive.webflux.server.master

import kr.disdong.spring.reactive.core.CoreApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(CoreApplication::class)
class MasterWebfluxServerApplication

fun main(args: Array<String>) {
    System.setProperty("server.port", "8080")
    System.setProperty("reactor.netty.ioWorkerCount", "10")
    runApplication<MasterWebfluxServerApplication>(*args)
}

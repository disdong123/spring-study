package kr.disdong.spring.reactive.study.mvc.server.slave

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
@RequestMapping("/")
class SlaveMvcServerApplication {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/thread-sleep")
    fun threadSleep(idx: String): String {
        logger.info("threadSleep()")
        Thread.sleep(1000)
        return "/threadSleep idx: $idx"
    }

    @GetMapping("/thread-sleep2")
    fun threadSleep2(idx: String): String {
        logger.info("threadSleep()")
        Thread.sleep(1000)
        return "/threadSleep2 idx: $idx"
    }
}

fun main(args: Array<String>) {
    System.setProperty("server.port", "8083")
    System.setProperty("reactor.netty.ioWorkerCount", "10")
    runApplication<SlaveMvcServerApplication>(*args)
}

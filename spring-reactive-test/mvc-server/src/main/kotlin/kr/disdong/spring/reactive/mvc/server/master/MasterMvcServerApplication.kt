package kr.disdong.spring.reactive.mvc.server.master

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@SpringBootApplication
@RestController
@RequestMapping("/")
class MasterMvcServerApplication {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val client = RestTemplate()

    @GetMapping("/thread-sleep")
    fun callWithThreadSleep(idx: String): String? {
        logger.info("callWithThreadSleep()")
        return client.getForObject("http://localhost:8083/thread-sleep?idx={idx}", String::class.java, idx)
    }
}

fun main(args: Array<String>) {
    System.setProperty("server.port", "8082")
    System.setProperty("reactor.netty.ioWorkerCount", "10")
    runApplication<MasterMvcServerApplication>(*args)
}

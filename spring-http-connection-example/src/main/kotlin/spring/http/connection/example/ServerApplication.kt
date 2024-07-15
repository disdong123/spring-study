package spring.http.connection.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
@RequestMapping("/test")
class ServerApplication {
    @GetMapping
    fun test(): String {
        Thread.sleep(3000)
        return "hello"
    }
}

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}

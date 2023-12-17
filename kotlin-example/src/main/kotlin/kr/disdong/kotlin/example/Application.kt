package kr.disdong.kotlin.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

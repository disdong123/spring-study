package spring.http.connection.example

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI

@SpringBootApplication
@RestController
@RequestMapping("/")
class ServerApplication(
    private val basicRestTemplate: RestTemplate,
    private val poolingRestTemplate: RestTemplate,
    private val defaultRestTemplate: RestTemplate,
    private val builderRestTemplate: RestTemplate,
    private val connectionTestTemplate: RestTemplate,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/basic")
    fun basic(): String {
        logger.warn("basicRestTemplate: ${basicRestTemplate.exchange(URI("https://www.socar.kr/"), HttpMethod.GET, null, String::class.java)}")
        return "hello"
    }

    @GetMapping("/pool")
    fun pool(): String {
        logger.warn("poolingRestTemplate: ${poolingRestTemplate.exchange(URI("https://www.socar.kr/"), HttpMethod.GET, null, String::class.java)}")
        return "hello"
    }

    @GetMapping("/default")
    fun default(): String {
        logger.warn("defaultRestTemplate: ${defaultRestTemplate.exchange(URI("https://www.socar.kr/"), HttpMethod.GET, null, String::class.java)}")
        return "hello"
    }

    @GetMapping("/builder")
    fun builder(): String {
        logger.warn("builderRestTemplate: ${builderRestTemplate.exchange(URI("https://www.socar.kr/"), HttpMethod.GET, null, String::class.java)}")
        return "hello"
    }

    @GetMapping("/connection-test")
    fun connectionTest(): String {
        logger.warn("connectionTestTemplate: ${connectionTestTemplate.exchange(URI("https://www.socar.kr/"), HttpMethod.GET, null, String::class.java)}")
        logger.warn("connectionTestTemplate: ${connectionTestTemplate.exchange(URI("https://www.socar.kr/guide"), HttpMethod.GET, null, String::class.java)}")
        logger.warn("connectionTestTemplate: ${connectionTestTemplate.exchange(URI("https://www.naver.com/"), HttpMethod.GET, null, String::class.java)}")
        logger.warn("connectionTestTemplate: ${connectionTestTemplate.exchange(URI("https://www.socar.kr/"), HttpMethod.GET, null, String::class.java)}")
        logger.warn("connectionTestTemplate: ${connectionTestTemplate.exchange(URI("https://www.naver.com/"), HttpMethod.GET, null, String::class.java)}")
        logger.warn("connectionTestTemplate: ${connectionTestTemplate.exchange(URI("https://map.naver.com/"), HttpMethod.GET, null, String::class.java)}")
        return "hello"
    }
}

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}

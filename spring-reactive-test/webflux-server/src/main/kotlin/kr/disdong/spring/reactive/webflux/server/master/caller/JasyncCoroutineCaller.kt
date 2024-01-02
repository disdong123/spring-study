package kr.disdong.spring.reactive.webflux.server.master.caller

import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/jasync-coroutine")
class JasyncCoroutineCaller {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val webClient = WebClient.create()
    private val restTemplate = RestTemplate()

    @GetMapping("/rest-template")
    fun callWithRestTemplate(idx: String): String? {
        logger.info("callWithRestTemplate()")
        return restTemplate.getForObject(
            "http://localhost:8081/jasync-coroutine?idx={idx}",
            String::class.java,
            idx
        )
    }

    @GetMapping("/web-client-reactive")
    fun callWithReactive(idx: String): Mono<String> {
        logger.info("callWithWebclient()")
        return webClient.get()
            .uri("http://localhost:8081/jasync-coroutine?idx={idx}", idx)
            .retrieve()
            .bodyToMono(String::class.java)
            .log()
    }

    @GetMapping("/web-client-coroutine")
    suspend fun callWithCoroutine(idx: String): String {
        logger.info("callWithCoroutine()")
        return webClient.get()
            .uri("http://localhost:8081/jasync-coroutine?idx={idx}", idx)
            .retrieve()
            .bodyToMono<String>()
            .awaitSingle()
    }
}

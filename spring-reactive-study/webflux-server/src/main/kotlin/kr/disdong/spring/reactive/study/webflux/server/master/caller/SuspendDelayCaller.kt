package kr.disdong.spring.reactive.study.webflux.server.master.caller

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
@RequestMapping("/suspend-delay")
class SuspendDelayCaller {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val webClient = WebClient.create()
    private val restTemplate = RestTemplate()

    @GetMapping("/web-client-reactive")
    fun callWithReactive(idx: String): Mono<String> {
        logger.info("callWithWebclient()")
        return webClient.get()
            .uri("http://localhost:8081/suspend-delay?idx={idx}", idx)
            .retrieve()
            .bodyToMono(String::class.java)
            .log()
    }

    @GetMapping("/rest-template")
    fun callWithRestTemplate(idx: String): String? {
        logger.info("callWithRestTemplate()")
        return restTemplate.getForObject(
            "http://localhost:8081/suspend-delay?idx={idx}",
            String::class.java,
            idx,
        )
    }

    @GetMapping("/web-client-coroutine")
    suspend fun callWithCoroutine(idx: String): String {
        logger.info("callWithCoroutine()")
        return webClient.get()
            .uri("http://localhost:8081/suspend-delay?idx={idx}", idx)
            .retrieve()
            .bodyToMono<String>()
            .awaitSingle()
    }
}

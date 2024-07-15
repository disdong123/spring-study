package spring.http.connection.example.restclient

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient

@RestController
class RestClientController(
    private val defaultRestClient: RestClient,
    private val httpComponentsClient: RestClient,
    private val reactorClient: RestClient,
    private val webClient: WebClient,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/restclient/default")
    fun defaultRestClient(): String {
        println("hello")
        logger.warn("defaultRestClient: ${defaultRestClient.get().uri("http://localhost:8080/test").retrieve().body(String::class.java)}")
        return "hello"
    }

    @GetMapping("/restclient/httpcomponents")
    fun httpComponentsClient(): String {
        logger.warn("httpComponentsClient: ${httpComponentsClient.get().uri("http://localhost:8080/test").retrieve()}")
        return "hello"
    }

    @GetMapping("/restclient/reactor")
    fun reactorClient(): String {
        logger.warn("reactorClient: ${reactorClient.get().uri("http://localhost:8080/test").retrieve().body(String::class.java)}")
        return "hello"
    }

    @GetMapping("/restclient/webclient")
    fun webclient(): String {
        logger.warn("webClient: ${webClient.get().uri("http://localhost:8080/test").retrieve().bodyToMono(Any::class.java).block()}")
        return "hello"
    }
}

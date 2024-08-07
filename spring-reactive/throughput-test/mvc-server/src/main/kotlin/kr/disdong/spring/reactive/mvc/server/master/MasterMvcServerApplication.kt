package kr.disdong.spring.reactive.mvc.server.master

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.apache.tomcat.util.threads.VirtualThreadExecutor
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ReactorNettyClientRequestFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@SpringBootApplication
@RestController
@RequestMapping("/")
class MasterMvcServerApplication(
    private val asyncDo: AsyncDo,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val template = RestTemplate()
    private val client = RestClient.builder().requestFactory(ReactorNettyClientRequestFactory()).build()
    private val webclient = WebClient.builder().build()
    private val es = Executors.newFixedThreadPool(10)
    private val vs = Executors.newVirtualThreadPerTaskExecutor()

    @GetMapping("/thread-sleep")
    fun callWithThreadSleep(idx: String): String? {
        logger.info("callWithThreadSleep()")
        return template.getForObject("http://localhost:8083/thread-sleep?idx={idx}", String::class.java, idx)
    }

    @GetMapping("/async-coroutine/rest-template")
    suspend fun coroutineRestTemplate(idx: String): String? {
        logger.info("${Thread.currentThread().name} coroutineRestTemplate($idx)")
        return withContext(es.asCoroutineDispatcher()) {
            logger.info("${Thread.currentThread().name} in coroutine")
            template.getForObject("http://localhost:8083/thread-sleep?idx={idx}", String::class.java, idx)
        }
    }

    @GetMapping("/async-coroutine/rest-client")
    suspend fun coroutineRestClient(idx: String): String? {
        logger.info("${Thread.currentThread().name} coroutineRestClient($idx)")
        return withContext(es.asCoroutineDispatcher()) {
            logger.info("${Thread.currentThread().name} in coroutine")
            client.get().uri("http://localhost:8083/thread-sleep?idx={idx}", idx).retrieve().body(String::class.java)
        }
    }

    @GetMapping("/async/web-client")
    fun coroutineWebClient(idx: String): Mono<String> {
        logger.info("${Thread.currentThread().name} coroutineWebClient($idx)")
        return webclient.get().uri("http://localhost:8083/thread-sleep?idx={idx}", idx).retrieve().bodyToMono(String::class.java)
    }

    @GetMapping("/async-vt/rest-template")
    suspend fun vtRestTemplate(idx: String): String? {
        logger.info("${Thread.currentThread().name} coroutineWebClient($idx)")
        logger.info("${Thread.currentThread().name} in coroutine")

        return withContext(vs.asCoroutineDispatcher()) {
            logger.info("${Thread.currentThread().name} in coroutine")
            template.getForObject("http://localhost:8083/thread-sleep?idx={idx}", String::class.java, idx)
        }
    }

    @GetMapping("/async/annotation")
    fun annotation() {
        asyncDo.`do`()
    }

    @GetMapping("/async-vt/annotation")
    fun vtAnnotation() {
        asyncDo.doVt()
    }
}

@Component
class AsyncDo {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Async("asyncDefaultAnnotationThreadPool")
    fun `do`() {
        logger.info("${Thread.currentThread().name} do()")
        Thread.sleep(5000)
    }

    @Async("asyncAnnotationThreadPool")
    fun doVt() {
        logger.info("${Thread.currentThread().name} do()")
        Thread.sleep(1000)
    }
}

@EnableAsync
@Configuration
class AsyncConfig {
    @Bean("asyncDefaultAnnotationThreadPool")
    fun asyncDefaultAnnotationThreadPool(): Executor {
        val executor = Executors.newFixedThreadPool(10)
        return executor
    }

    @Bean("asyncAnnotationThreadPool")
    fun asyncAnnotationThreadPool(): Executor {
        val executor = VirtualThreadExecutor("async-annotation-")
        return executor
    }
}

fun main(args: Array<String>) {
    System.setProperty("server.port", "8082")
    System.setProperty("server.tomcat.threads.max", "10")
    // System.setProperty("spring.threads.virtual.enabled", "true")
    runApplication<MasterMvcServerApplication>(*args)
}

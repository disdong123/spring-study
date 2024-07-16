package kr.disdong.spring.reactive.webflux.server.slave

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.ResultSet
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.reactor.awaitSingle
import kr.disdong.spring.reactive.core.CoreApplication
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.delay
import java.time.Duration
import java.util.concurrent.CompletableFuture

@SpringBootApplication
@Import(CoreApplication::class)
@RestController("/")
class SlaveWebfluxServerApplication(
    private val jConnection: Connection,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/thread-sleep")
    fun threadSleep(idx: String): String {
        logger.info("threadSleep()")
        Thread.sleep(1000)
        return "/threadSleep idx: $idx"
    }

    @GetMapping("/mono-delay")
    fun monoDelay(idx: String): Mono<String> {
        logger.info("monoDelay()")
        return Mono.just("/monoDelay idx: $idx")
            .delayElement(Duration.ofSeconds(1))
    }

    @GetMapping("/suspend-delay")
    suspend fun suspendDelay(idx: String): String {
        logger.info("suspendDelay()")
        delay(Duration.ofSeconds(1)).awaitSingle()
        return "/suspendDelay idx: $idx"
    }

    @GetMapping("/jasync-block")
    fun jasyncBlock(): ResultSet {
        logger.info("jasyncBlock()")
        return jConnection.sendPreparedStatement("DO SLEEP(1)")
            .get()
            .rows
    }

    @GetMapping("/jasync-completable-future")
    fun jasyncCompletableFuture(): CompletableFuture<QueryResult> {
        logger.info("jasyncCompletableFuture()")
        return jConnection.sendPreparedStatement("DO SLEEP(1)")
    }

    @GetMapping("/jasync-mono")
    suspend fun jasyncMono(idx: String): String {
        logger.info("jasyncMono()")
        Mono.fromFuture(jConnection.sendPreparedStatement("DO SLEEP(1)"))
        return "/jasyncMono idx: $idx"
    }

    @GetMapping("/jasync-coroutine")
    suspend fun jasyncCoroutine(idx: String): String {
        logger.info("jasyncCoroutine()")
        jConnection.sendPreparedStatement("DO SLEEP(1)").asDeferred().await()
        return "/jasyncCoroutine idx: $idx"
    }
}

fun main(args: Array<String>) {
    System.setProperty("server.port", "8081")
    System.setProperty("reactor.netty.ioWorkerCount", "10")
    runApplication<SlaveWebfluxServerApplication>(*args)
}

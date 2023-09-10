package kr.disdong.spring.reactive.study.server.test

import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch
import org.springframework.web.client.RestTemplate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

var counter = AtomicInteger(0)

enum class TestType(val url: String) {
    // TODO delay, thread sleep 으로 테스트합니다.
    THREAD_SLEEP_WITH_REST_TEMPLATE("/thread-sleep/rest-template"),
    THREAD_SLEEP_WITH_WEB_CLIENT_REACTIVE("/thread-sleep/web-client-reactive"),
    THREAD_SLEEP_WITH_WEB_CLIENT_COROUTINE("/thread-sleep/web-client-coroutine"),

    MONO_DELAY_WITH_REST_TEMPLATE("/mono-delay/rest-template"),
    MONO_DELAY_WITH_WEB_CLIENT_REACTIVE("/mono-delay/web-client-reactive"),
    MONO_DELAY_WITH_WEB_CLIENT_COROUTINE("/mono-delay/web-client-coroutine"),

    SUSPEND_DELAY_WITH_REST_TEMPLATE("/suspend-delay/rest-template"),
    SUSPEND_DELAY_WITH_WEB_CLIENT_REACTIVE("/suspend-delay/web-client-reactive"),
    SUSPEND_DELAY_WITH_WEB_CLIENT_COROUTINE("/suspend-delay/web-client-coroutine"),

    // TODO jasync 로 테스트합니다.
    JASYNC_BLOCK_WITH_REST_TEMPLATE("/jasync-coroutine/rest-template"),
    JASYNC_BLOCK_WITH_WEB_CLIENT_REACTIVE("/jasync-coroutine/web-client-reactive"),
    JASYNC_BLOCK_WITH_WEB_CLIENT_COROUTINE("/jasync-coroutine/web-client-coroutine"),

    JASYNC_COMPLETABLE_FUTURE_WITH_REST_TEMPLATE("/jasync-completable-future/rest-template"),
    JASYNC_COMPLETABLE_FUTURE_WITH_WEB_CLIENT_REACTIVE("/jasync-completable-future/web-client-reactive"),
    JASYNC_COMPLETABLE_FUTURE_WITH_WEB_CLIENT_COROUTINE("/jasync-completable-future/web-client-coroutine"),

    JASYNC_COROUTINE_WITH_REST_TEMPLATE("/jasync-coroutine/rest-template"),
    JASYNC_COROUTINE_WITH_WEB_CLIENT_REACTIVE("/jasync-coroutine/web-client-reactive"),
    JASYNC_COROUTINE_WITH_WEB_CLIENT_COROUTINE("/jasync-coroutine/web-client-coroutine"),
}

fun test(threadPoolSize: Int, loopCount: Int, testType: TestType) {
    val logger = LoggerFactory.getLogger(testType.name)
    val client = RestTemplate()
    val executorService = Executors.newFixedThreadPool(threadPoolSize)
    val functionTimer = StopWatch()

    functionTimer.start()

    for (i in 1..loopCount) {
        executorService.execute {
            val loopTimer = StopWatch()
            val idx = counter.addAndGet(1)
            logger.info("[Thread number: $idx] Start")

            loopTimer.start()
            val res = client.getForObject("http://localhost:8080/${testType.url}?idx={idx}", String::class.java, idx)
            loopTimer.stop()

            logger.info("[Thread number: $idx] end. Running time: ${loopTimer.totalTimeSeconds} / $res")
        }
    }

    executorService.shutdown()
    executorService.awaitTermination(100, TimeUnit.SECONDS)
    functionTimer.stop()

    logger.info("Total: ${functionTimer.totalTimeSeconds}")
}

fun main() {
    // test(20, 20, TestType.THREAD_SLEEP_WITH_REST_TEMPLATE) // 2 ~ . 연속으로 하면 그 다음건 4초정도 걸리는데 이유가?
    // test(20, 20, TestType.THREAD_SLEEP_WITH_WEB_CLIENT_REACTIVE) // 2 ~
    // test(20, 20, TestType.THREAD_SLEEP_WITH_WEB_CLIENT_COROUTINE) // 2 ~

    // test(20, 20, TestType.MONO_DELAY_WITH_REST_TEMPLATE) // 2 ~
    // test(20, 20, TestType.MONO_DELAY_WITH_WEB_CLIENT_REACTIVE) // 1 ~
    // test(20, 20, TestType.MONO_DELAY_WITH_WEB_CLIENT_COROUTINE) // 1 ~

    // test(20, 20, TestType.SUSPEND_DELAY_WITH_REST_TEMPLATE) // 2 ~
    // test(20, 20, TestType.SUSPEND_DELAY_WITH_WEB_CLIENT_REACTIVE) // 1 ~
    // test(20, 20, TestType.SUSPEND_DELAY_WITH_WEB_CLIENT_COROUTINE) // 1 ~

    // test(20, 20, TestType.JASYNC_BLOCK_WITH_REST_TEMPLATE) // 2 ~
    // test(20, 20, TestType.JASYNC_BLOCK_WITH_WEB_CLIENT_REACTIVE) // 1 ~
    // test(20, 20, TestType.JASYNC_BLOCK_WITH_WEB_CLIENT_COROUTINE) // 1 ~

    // test(20, 20, TestType.JASYNC_COMPLETABLE_FUTURE_WITH_REST_TEMPLATE) // 2 ~
    // test(20, 20, TestType.JASYNC_COMPLETABLE_FUTURE_WITH_WEB_CLIENT_REACTIVE) // 1 ~
    // test(20, 20, TestType.JASYNC_COMPLETABLE_FUTURE_WITH_WEB_CLIENT_COROUTINE) // 1 ~

    // test(20, 20, TestType.JASYNC_COROUTINE_WITH_REST_TEMPLATE) // 2 ~
    test(20, 20, TestType.JASYNC_COROUTINE_WITH_WEB_CLIENT_REACTIVE) // 1 ~
    // test(20, 20, TestType.JASYNC_COROUTINE_WITH_WEB_CLIENT_COROUTINE) // 1~
}

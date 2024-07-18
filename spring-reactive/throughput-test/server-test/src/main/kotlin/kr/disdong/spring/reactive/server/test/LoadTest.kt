package kr.disdong.spring.reactive.server.test

import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch
import org.springframework.web.client.RestTemplate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

var counter = AtomicInteger(0)

enum class TestType(val url: String) {
    // TODO mvc
    MVC_ASYNC_COROUTINE_WITH_REST_TEMPLATE("/async-coroutine/rest-template"),
    MVC_ASYNC_COROUTINE_WITH_REST_CLIENT("/async-coroutine/rest-client"),
    MVC_ASYNC_REACTIVE_WITH_WEB_CLIENT("/async-reactive/web-client"),

    MVC_ASYNC_VIRTUAL_THREAD_WITH_REST_TEMPLATE("/async-vt/rest-template"),
    MVC_ASYNC_VIRTUAL_THREAD_WITH_REST_CLIENT("/async-vt/rest-client"),

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

fun test(threadPoolSize: Int, loopCount: Int, testType: TestType, port: Int = 8080) {
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
            val res = client.getForObject("http://localhost:$port/${testType.url}?idx={idx}", String::class.java, idx)
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
    /**
     * total: 2.x
     * 코루틴 내의 blocking io 로 인해 스레드를 블록한다.
     */
    // test(20, 20, TestType.MVC_ASYNC_COROUTINE_WITH_REST_TEMPLATE, 8082)
    /**
     * total: 2.x
     * reactor-netty factory 를 사용해도 리턴 타입 자체가 publisher 가 아니라 결국 blocking 되는 듯 하다.
     * 위와 마찬가지로 코루틴 내의 blocking 으로 인해 스레드가 블록된다.
     * rest client 를 사용하는게 어떤 의미가 있는지 잘 모르겠다.
     */
    // test(20, 20, TestType.MVC_ASYNC_COROUTINE_WITH_REST_CLIENT, 8082)
    /**
     * total: 1.x
     * webclient.retrieve.block() 만 수행하지 않으면 blocking 없이 잘 수행된다.
     * block() 을 호출하면 이벤트 루프가 blocking 되어 처리량이 떨어진다.
     * 즉, mvc 에서 webclient 를 사용해도 block() 을 이용하면 별로 의미가 없다.
     */
    // test(20, 20, TestType.MVC_ASYNC_REACTIVE_WITH_WEB_CLIENT, 8082)

    /**
     * total: 1.x
     * blocking io 로 200개를 실행해도 10개의 스레드로 전부 처리한다.
     * forkjoinpool 이라는 스레드 풀이 생기는데, 얘가 가상 스레드를 처리해주는 걸로 보임.
     * 플랫폼 스레드가 blocking 되지 않고 잘 실행된다...
     *
     * 지금은 withContext 에 virtual thread pool 을 넣는 방식으로 비동기 처리했는데 이게 올바른 테스트가 맞는지 확인이 필요할 듯.
     * 확실히 visual vm 으로 봤을때 스레드가 더 생기지는 않는다.
     */
    test(200, 200, TestType.MVC_ASYNC_VIRTUAL_THREAD_WITH_REST_TEMPLATE, 8082)

    /**
     * total: 2.x or 3.x
     * slave 에서는 같은 스레드가 두개의 3개의 요청을 처리할 때가 있다.
     * 예를 들면, 어떤 스레드는 하나만 처리하고 어떤 스레드는 3개를 처리하여 전체 처리 속도가 3초가 나올 때가 있다.
     * 이게 어떻게 가능할까...?
     */
    // test(20, 20, TestType.THREAD_SLEEP_WITH_REST_TEMPLATE)
    /**
     * total: 2.x
     * slave 의 스레드가 10개 뿐이므로 webclient 를 이용해도 2초가 걸린다.
     */
    // test(20, 20, TestType.THREAD_SLEEP_WITH_WEB_CLIENT_REACTIVE)
    /**
     * total: 2.x
     * slave 의 스레드가 10개 뿐이므로 webclient 를 이용해도 2초가 걸린다.
     */
    // test(20, 20, TestType.THREAD_SLEEP_WITH_WEB_CLIENT_COROUTINE)

    // test(100, 20, TestType.MONO_DELAY_WITH_REST_TEMPLATE) // 2 ~
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
    // test(20, 20, TestType.JASYNC_COROUTINE_WITH_WEB_CLIENT_REACTIVE) // 1 ~
    // test(20, 20, TestType.JASYNC_COROUTINE_WITH_WEB_CLIENT_COROUTINE) // 1~
}

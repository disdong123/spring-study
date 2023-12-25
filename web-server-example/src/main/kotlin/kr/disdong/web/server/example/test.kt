package kr.disdong.web.server.example

import kr.disdong.core.Clogger
import org.springframework.util.StopWatch
import java.net.Socket
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SimpleClient(private val hostName: String = "localhost", private val port: Int, private val message: String) {

    private val logger = Clogger<SimpleClient>()
    fun run() {
        val socket = Socket(hostName, port)
        logger.info("Client connected: ${socket.inetAddress.hostAddress}")

        val reader = socket.inputStream.bufferedReader()
        val writer = socket.outputStream.bufferedWriter()

        writer.write(message)
        writer.newLine()
        writer.flush()

        logger.info("response from server: ${reader.readLine()}")

        reader.close()
        writer.close()
        socket.close()
    }
}

fun test(loopCount: Int, port: Int) {
    val logger = Clogger<SimpleClient>()
    val executorService = Executors.newFixedThreadPool(loopCount)
    val functionTimer = StopWatch()
    val client = SimpleClient(port = port, message = "hello, world")

    functionTimer.start()

    for (i in 1..loopCount) {
        executorService.execute {
            val loopTimer = StopWatch()

            loopTimer.start()
            client.run()
            loopTimer.stop()
            logger.info("[Loop Number: $i] end. Running time: ${loopTimer.totalTimeSeconds}")
        }
    }

    executorService.shutdown()
    executorService.awaitTermination(100, TimeUnit.SECONDS)
    functionTimer.stop()

    logger.info("Total: ${functionTimer.totalTimeSeconds}")
}

fun main() {
    /** 모든 요청은 Thread.sleep(3000) 이 적용됩니다. */

    // single thread
    // 3초에 한번씩 요청을 처리합니다.
    Thread(SingleThreadServer(18080)).start()
    Thread.sleep(100) // cushion time
    test(loopCount = 10, port = 18080)

    // multi thread
    // 한번에 요청을 처리합니다.
    Thread(MultiThreadServer(18081)).start()
    Thread.sleep(100)
    test(loopCount = 10, port = 18081)

    // multi flexing
    // 여러 요청을 빠르게 받을 수 있지만 응답은 thread.sleep 으로 블로킹되어 3초에 한번씩 응답이 옵니다.
    Thread(MultiFlexingServer(18082)).start()
    Thread.sleep(100)
    test(loopCount = 10, port = 18082)

    // event loop
    // multi flexing 과 동일합니다.
    Thread(EventLoop(18083)).start()
    Thread.sleep(100)
    test(10, 18083)

    // netty
    // worker thread 를 1개로 설정하면 multi flexing 과 동일합니다.
    Thread(NettyServer(18084, 1)).start()
    Thread.sleep(1000)
    test(10, 18084)
}

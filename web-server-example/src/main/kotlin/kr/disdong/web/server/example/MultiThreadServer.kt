package kr.disdong.web.server.example

import kr.disdong.core.Clogger
import java.net.ServerSocket
import java.net.Socket

class MultiThreadServer(private val port: Int) : Runnable {
    private val logger = Clogger<MultiThreadServer>()
    override fun run() {
        logger.info("Server started at port $port")
        val serverSocket = ServerSocket(port)

        while (true) {
            val clientSocket = serverSocket.accept()
            logger.info("Client connected: ${clientSocket.inetAddress.hostAddress}")
            Thread(WorkerThread(clientSocket)).start()
        }
    }
}

class WorkerThread(
    private val clientSocket: Socket
) : Runnable {
    private val logger = Clogger<WorkerThread>()
    override fun run() {
        logger.info("Worker thread started")
        Thread.sleep(3000)
        val reader = clientSocket.inputStream.bufferedReader()
        val writer = clientSocket.outputStream.bufferedWriter()

        var s = reader.readLine()
        while (s != null) {
            if (s == "") {
                break
            }
            writer.write(s)
            writer.newLine()
            writer.flush()
            s = reader.readLine()
        }

        logger.info("Worker thread terminated")
        reader.close()
        writer.close()
        clientSocket.close()
    }
}

package kr.disdong.web.server.example

import kr.disdong.core.Clogger
import java.net.ServerSocket

class SingleThreadServer(private val port: Int) : Runnable {

    private val logger = Clogger<SingleThreadServer>()
    override fun run() {
        logger.info("Server started at port $port")
        val serverSocket = ServerSocket(port)

        while (true) {
            val clientSocket = serverSocket.accept()
            logger.info("Client connected: ${clientSocket.inetAddress.hostAddress}")
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
            reader.close()
            writer.close()
            clientSocket.close()

            logger.info("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
        }
    }
}

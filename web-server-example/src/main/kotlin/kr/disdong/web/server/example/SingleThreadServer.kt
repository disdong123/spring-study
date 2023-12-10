package kr.disdong.web.server.example

import java.net.ServerSocket

class SingleThreadServer(private val port: Int) : Runnable {
    override fun run() {
        println("Server started at port $port")
        val serverSocket = ServerSocket(port)

        while (true) {
            val clientSocket = serverSocket.accept()
            println("Client connected: ${clientSocket.inetAddress.hostAddress}")
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

            println("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
        }
    }
}

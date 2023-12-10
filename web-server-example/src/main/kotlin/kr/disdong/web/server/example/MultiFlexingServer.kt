package kr.disdong.web.server.example

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class MultiFlexingServer(private val port: Int) : Runnable {

    override fun run() {
        val selector = Selector.open()

        // ServerSocketChannel 는 TCP 연결 요청이 올때까지 대기하다가 요청이 들어오면 연결을 수락하는 채널입니다.
        val serverSocketChannel = ServerSocketChannel.open()
        serverSocketChannel.bind(InetSocketAddress("localhost", port))

        // 셀렉터에 채널을 등록하려면 채널이 non-blocking 모드여야 합니다.
        serverSocketChannel.configureBlocking(false)

        // 확인하고자 하는 이벤트 종류와 함께 채널을 셀렉터에 등록합니다.
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)
        val buffer = ByteBuffer.allocate(256)

        while (true) {
            println("before select()...")
            // 채널에 이벤트가 발생할 때까지 대기합니다.
            // 이벤트를 처리할 수 있는 채널이 있으면 채널의 수를 반환합니다.
            selector.select()
            println("after select()...")

            val iter = selector.selectedKeys().iterator()
            // 이벤트가 발생한 채널에 접근합니다.
            while (iter.hasNext()) {
                val key = iter.next()
                println("key: $key")

                // 커넥션이 연결되면 연결에 대한 이벤트가 발생하고, ServerSocketChannel 로 accept 를 하면 SocketChannel 이 반환됩니다.
                // 반환된 SocketChannel 을 다시 셀렉터에 등록하면 데이터가 읽을 준비가 되었을 때 이벤트가 발생하고, 적절한 처리를 해주면 됩니다.

                // 새로운 소켓 커넥션을 받을 준비가 되었을 때.
                if (key.isAcceptable) {
                    accept(selector, serverSocketChannel)
                }

                // 데이터를 읽을 준비가 되었을 때.
                if (key.isReadable) {
                    echo(buffer, key)
                }

                iter.remove()
            }
        }
    }

    /**
     * 연결을 accept 하고 반환된 채널을 셀렉터에 등록합니다.
     * @param selector
     * @param channel
     */
    private fun accept(selector: Selector, channel: ServerSocketChannel) {
        println("register...")
        val clientSocketChannel = channel.accept()
        clientSocketChannel.configureBlocking(false)
        clientSocketChannel.register(selector, SelectionKey.OP_READ)
    }

    private fun echo(buffer: ByteBuffer, key: SelectionKey) {
        println("echo...")
        val client = key.channel() as SocketChannel
        client.read(buffer)
        buffer.flip()
        Thread.sleep(3000)
        client.write(buffer)
        client.close()
        buffer.clear()
    }
}

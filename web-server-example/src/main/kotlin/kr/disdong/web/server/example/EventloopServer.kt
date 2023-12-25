package kr.disdong.web.server.example

import kr.disdong.core.Clogger
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class EventLoop(
    private val port: Int,
) : Runnable {
    private val logger = Clogger<EventLoop>()
    private var selector: Selector? = null
    private var serverSocketChannel: ServerSocketChannel? = null

    init {
        selector = Selector.open()

        serverSocketChannel = ServerSocketChannel.open()
        serverSocketChannel!!.socket().bind(InetSocketAddress("localhost", port))
        serverSocketChannel!!.configureBlocking(false)
        val key = serverSocketChannel!!.register(selector, SelectionKey.OP_ACCEPT)

        // serverSocketChannel 에서 이벤트가 발생했을 때 이벤트를 처리하기위해 Handler 를 attach 해줍니다.
        // 이후 attachment 메서드로 Handler 를 가져올 수 있습니다.
        key.attach(AcceptHandler(selector!!, serverSocketChannel!!))
    }

    override fun run() {
        logger.info("Reactor started")

        while (true) {
            logger.info("before select()...")
            selector!!.select()
            logger.info("after select()...")

            val iter = selector!!.selectedKeys().iterator()
            while (iter.hasNext()) {
                val key = iter.next()
                dispatch(key)
                iter.remove()
            }
        }
    }

    /**
     * key 에 attach 된 Handler 를 가져와서 이벤트를 처리합니다.
     * @param key
     */
    private fun dispatch(key: SelectionKey) {
        val handler = key.attachment() as Handler
        handler.handle()
    }
}

interface Handler {
    fun handle()
}

class AcceptHandler(
    private val selector: Selector,
    private val serverSocketChannel: ServerSocketChannel,
) : Handler {
    private val logger = Clogger<AcceptHandler>()
    override fun handle() {
        logger.info("AcceptHandler.handle() start...")

        try {
            val socketChannel = serverSocketChannel.accept()
            if (socketChannel != null) {
                EchoHandler(selector, socketChannel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class EchoHandler(
    private val selector: Selector,
    private val socketChannel: SocketChannel,
) : Handler {

    private val logger = Clogger<EchoHandler>()
    private var selectionKey: SelectionKey? = null
    private val buffer = ByteBuffer.allocate(256)
    private var state = State.READ

    init {
        logger.info("EchoHandler.init() start...")
        socketChannel.configureBlocking(false)
        selectionKey = socketChannel.register(selector, SelectionKey.OP_READ)
        selectionKey!!.attach(this)
        selector.wakeup()
    }

    override fun handle() {
        logger.info("EchoHandler.handle() start...")
        try {
            if (state == State.READ) {
                logger.info("EchoHandler.handle() read...")
                val readCount = socketChannel.read(buffer)
                if (readCount > 0) {
                    buffer.flip()
                }

                selectionKey!!.interestOps(SelectionKey.OP_WRITE)
                state = State.WRITE
            } else if (state == State.WRITE) {
                logger.info("EchoHandler.handle() write...")
                Thread.sleep(3000)
                socketChannel.write(buffer)
                buffer.clear()
                selectionKey!!.interestOps(SelectionKey.OP_READ)
                state = State.READ
                socketChannel.close()
            }
        } catch (err: Exception) {
            err.printStackTrace()
        }
    }
}

enum class State {
    READ,
    WRITE,
}

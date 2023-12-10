package kr.disdong.web.server.example

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler

class NettyServer(
    private val port: Int,
    private val workerThreadCount: Int,
) : Runnable {
    override fun run() {
        println("NettyServer started...")
        val mainGroup = NioEventLoopGroup(10)
        val workerGroup = NioEventLoopGroup(workerThreadCount)
        try {
            val serverBootstrap = io.netty.bootstrap.ServerBootstrap()
            serverBootstrap.group(mainGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, 108)
                .handler(LoggingHandler(LogLevel.DEBUG))
                .childHandler(WorkerHandler())

            val server = serverBootstrap.bind(port).sync()

            server.channel().closeFuture().sync()
        } finally {
            mainGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}

class WorkerHandler : ChannelInitializer<SocketChannel>() {
    private val nettyEchoHandler = NettyEchoHandler()
    override fun initChannel(ch: SocketChannel) {
        println("initChannel start...")
        val p = ch.pipeline()
        p.addLast(nettyEchoHandler)
    }
}

@Sharable
class NettyEchoHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        println("${Thread.currentThread()}  channelRead start...")
        Thread.sleep(3000)
        ctx.write(msg)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // Close the connection when an exception is raised.
        cause.printStackTrace()
        ctx.close()
    }
}

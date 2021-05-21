package com.yfker.server.netty.center;

import com.yfker.common.constant.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-09-27 16:11
 **/
@Slf4j
public class MsgServer {

    private int port;

    public MsgServer(int port) {
        this.port = port;
    }

    public void start() {
        //创建两个线程组bossGroup和workerGroup, 含有的子线程NioEventLoop的个数默认为cpu核数的两 倍
        // bossGroup只是处理连接请求 ,真正的和客户端业务处理，会交给workerGroup完成
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来配置参数
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    // HttpRequestDecoder和HttpResponseEncoder的一个组合，针对http协议进行编解码
                                    .addLast(new HttpServerCodec())
                                    // 分块向客户端写数据，防止发送大文件时导致内存溢出， channel.write(new ChunkedFile(new File("bigFile.mkv")))
                                    .addLast(new ChunkedWriteHandler())
                                    // 将HttpMessage和HttpContents聚合到一个完成的 FullHttpRequest或FullHttpResponse中,具体是FullHttpRequest对象还是FullHttpResponse对象取决于是请求还是响应
                                    // 需要放到HttpServerCodec这个处理器后面
                                    .addLast(new HttpObjectAggregator(10240))
                                    // webSocket 数据压缩扩展，当添加这个的时候WebSocketServerProtocolHandler的第三个参数需要设置成true
                                    .addLast(new WebSocketServerCompressionHandler())
                                    //编码格式
                                    .addLast(new StringEncoder(Charset.forName("UTF-8")))
                                    //解码格式
                                    .addLast(new StringDecoder(Charset.forName("UTF-8")))
                                    //对客户端，如果在60秒内没有向服务端发送心跳，就主动断开
                                    //三个参数分别为读/写/读写的空闲，我们只针对读空闲检测
                                    .addLast(new IdleStateHandler(120, 0, 0, TimeUnit.SECONDS))
                                    // 自定义处理器 - 处理 web socket 文本消息
                                    .addLast(new MsgServerHandler())
                                    // 自定义处理器 - 处理 web socket 二进制消息
//                                    .addLast(new BinaryWebSocketFrameHandler())
                                    // 服务器端向外暴露的 web socket 端点，当客户端传递比较大的对象时，maxFrameSize参数的值需要调大
                                    .addLast(new WebSocketServerProtocolHandler(Constants.DEFAULT_WEB_SOCKET_LINK, null, true, 10485760));

                        }
                    });
            log.info("message server start... port[{}]", port);
            ChannelFuture cf = bootstrap.bind(port).sync();
            //对通道关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
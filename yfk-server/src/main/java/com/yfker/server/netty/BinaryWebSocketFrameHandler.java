package com.yfker.server.netty;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 二进制消息处理
 * @author: lijiayu
 * @date: 2020-09-29 13:48
 **/
@Slf4j
public class BinaryWebSocketFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
        log.info("服务器接收到二进制消息. [{}]", msg.toString());
        ByteBuf content = msg.content();
        content.markReaderIndex();
        int flag = content.readInt();
        log.info("标志位:[{}]", flag);
        content.resetReaderIndex();

        ByteBuf byteBuf = Unpooled.directBuffer(msg.content().capacity());
        byteBuf.writeBytes(msg.content());

        //转成byte
        byte[] bytes = new byte[msg.content().capacity()];
        byteBuf.readBytes(bytes);
        //byte转ByteBuf
        ByteBuf byteBuf2 = Unpooled.directBuffer(bytes.length);
        byteBuf2.writeBytes(bytes);


        log.info("JSON.toJSONString(byteBuf) [ {} ]", JSON.toJSONString(byteBuf));
        //TODO 这是发给自己
        ctx.writeAndFlush(new BinaryWebSocketFrame(byteBuf));


    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加
        log.info(" 客户端加入 [ {} ]", ctx.channel().id().asLongText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除
        log.info(" 离线 [ {} ] ", ctx.channel().id().asLongText());
    }
}
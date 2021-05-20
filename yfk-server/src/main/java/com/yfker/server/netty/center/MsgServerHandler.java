package com.yfker.server.netty.center;

import com.yfker.common.constant.Constants;
import com.yfker.server.utils.MyBeansUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @description: 消息处理类
 * @author: lijiayu
 * @date: 2020-09-27 16:25
 **/
@Slf4j
public class MsgServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    static final String heartCheckStr = "yfk888_ALIVE";

    /**
     * GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 在线用户的容器
     */
    OnlineDeviceContainer onlineContainer;

    public MsgServerHandler() {
        onlineContainer = MyBeansUtils.getBean(OnlineDeviceContainer.class);
    }

    /**
     * 表示 channel 处于不活动状态，提示离线了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        onlineContainer.remove(channel.id().asLongText());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        //判断evt是否是IdleStateEvent(用于触发用户事件，包含读空闲/写空闲/读写空闲)
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("进入读空闲...");
                //关闭并移除无用channel，避免浪费资源
                onlineContainer.remove(ctx.channel().id().asLongText());
                ctx.close();
            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("进入写空闲...");
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                log.info("进入读写空闲...");
            }
        }
    }

    /**
     * 处理异常, 一般是需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //移除
        onlineContainer.remove(ctx.channel().id().asLongText());
        ctx.close();
        log.error("服务器发生了异常: [ {} ]", cause);
    }

    /**
     * 经过测试，在 ws 的 uri 后面不能传递参数，不然在 netty 实现 websocket 协议握手的时候会出现断开连接的情况。
     * 针对这种情况在 websocketHandler 之前做了一层 地址过滤，然后重写
     * request 的 uri，并传入下一个管道中，基本上解决了这个问题。
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            log.info("调用 channelRead request.uri() [ {} ]", request.uri());
            log.info("Origin [ {} ] [ {} ]", request.headers().get("Origin"), request.headers().get("Host"));
            String Origin = request.headers().get("Origin");
            if (null == Origin) {
                log.info("Origin 为空 ");
                ctx.close();
            } else {
                String esm = getParam(request.uri(), "esm");
                String type = getParam(request.uri(), "type");
                if (!StringUtils.isEmpty(type)) {
                    try {
                        int itype = Integer.parseInt(type);
                        OnlineDevice device = new OnlineDevice();
                        device.setEsm(esm);
                        device.setType(itype);
                        device.setChannelId(ctx.channel().id().asLongText());
                        device.setCtx(ctx);
                        onlineContainer.putDevice(device);
                    } catch (NumberFormatException e) {
                        log.info("不允许 [ {} ] 连接 强制断开, type:{}", Origin, type);
                        ctx.close();
                    }
                    request.setUri(Constants.DEFAULT_WEB_SOCKET_LINK);
                } else {
                    log.info("不允许 [ {} ] 连接 强制断开, type isEmpty", Origin);
                    ctx.close();
                }
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame tmsg) throws Exception {
        if (tmsg.text().equals(heartCheckStr)) {
            // 心跳数据不予处理
            return;
        }
        // 处理消息
        onlineContainer.postMsg(tmsg);
    }

    private String getParam(String url, String name) {
        String[] urlParts = url.split("\\?");
        //没有参数
        if (urlParts.length == 1) {
            return null;
        }
        //有参数
        String[] params = urlParts[1].split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (name.equals(keyValue[0])) {
                return keyValue[1];
            }
        }
        return null;
    }


}
package com.yfker.server.netty;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-09-29 15:05
 **/
@Data
public class OnlineUser {
    String id;
    String name;
    String channelId;
    String meetNumber;
    ChannelHandlerContext ctx;
}

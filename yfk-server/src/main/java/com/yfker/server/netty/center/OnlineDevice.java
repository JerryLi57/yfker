package com.yfker.server.netty.center;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @description: 在线设备的基础信息
 * @author: lijiayu
 * @date: 2020-09-29 15:05
 **/
@Data
public class OnlineDevice {
    /**
     * 设备编号  Equipment serial number
     */
    String esm;

    /**
     * 消息类型 1:web, 2:app
     */
    int type;

    String channelId;
    ChannelHandlerContext ctx;
}

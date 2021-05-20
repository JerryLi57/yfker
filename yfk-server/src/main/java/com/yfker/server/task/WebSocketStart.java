package com.yfker.server.task;

import com.yfker.server.config.NettyConfig;
import com.yfker.server.netty.center.MsgServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-11-12 15:43
 **/
@Configuration
@Component
public class WebSocketStart implements ApplicationRunner {

    @Autowired
    NettyConfig nettyConfig;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        new Thread(() -> new MsgServer(nettyConfig.getWebsocketPort()).start()).start();
    }


}

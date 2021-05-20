package com.yfker.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: 聊天服务配置文件
 * @author: lijiayu
 * @date: 2021-01-08 16:38
 **/
@Data
@Component
@ConfigurationProperties("netty")
public class NettyConfig {

    private int websocketPort;
}

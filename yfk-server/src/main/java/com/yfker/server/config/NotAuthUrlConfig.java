package com.yfker.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @description: 网关跳过认证的配置类
 * @author: lijiayu
 * @date: 2020-03-18 15:36
 **/
@Data
@Component
@ConfigurationProperties("gateway")
public class NotAuthUrlConfig {

    /**
     * 配置需要跳过认证的微服务url
     */
    private ArrayList<String> shouldSkipUrls;

    /**
     * 配置需要跳过但是还是需要登录认证的微服务url
     */
    private ArrayList<String> shouldSkipButNeedLoginUrls;
}

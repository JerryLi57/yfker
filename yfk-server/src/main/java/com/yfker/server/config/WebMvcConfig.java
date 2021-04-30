package com.yfker.server.config;

import com.yfker.server.interceptors.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: WebMvcConfig
 * @author: lijiayu
 * @date: 2020-07-10 16:08
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //静态资源不拦截
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**");
    }
}

package com.yfker.server.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-09-29 16:22
 **/
@Component
public class MyBeansUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MyBeansUtils.context = applicationContext;
    }

    public static <T> T getBean(Class<T> bean) {
        return context.getBean(bean);
    }

    public static <T> T getBean(String var1, @Nullable Class<T> var2) {
        return context.getBean(var1, var2);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}

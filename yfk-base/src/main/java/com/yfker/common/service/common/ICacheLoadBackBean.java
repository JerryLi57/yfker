package com.yfker.common.service.common;

/**
 * @description: 加载缓存的接口, 用于JavaBean缓存
 * @author: lijiayu
 * @date: 2020-03-20 15:06
 **/
public interface ICacheLoadBackBean<T> {

    /**
     * 加载数据
     *
     * @return
     */
    T load();
}

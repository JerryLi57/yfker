package com.yfker.common.service.common;

import java.util.List;

/**
 * @description: 加载缓存的接口, 用于List缓存
 * @author: lijiayu
 * @date: 2020-03-20 15:06
 **/
public interface ICacheLoadBackList<T> {

    /**
     * 加载数据
     *
     * @return
     */
    List<T> load();
}

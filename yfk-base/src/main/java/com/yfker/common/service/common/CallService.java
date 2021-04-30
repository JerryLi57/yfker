package com.yfker.common.service.common;

/**
 * @description: CallService 定义调用service的接口
 * @author: lijiayu
 * @date: 2020-03-11 20:37
 **/
public interface CallService<V> {

    /**
     * 业务封装的方法回调名称定义
     * @return
     * @throws Exception
     */
    V call() throws Exception;

}

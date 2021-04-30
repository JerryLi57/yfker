package com.yfker.server.pool;

/**
 * @description: 任务接口定义规范
 * @author: lijiayu
 * @date: 2020-05-15 11:26
 **/
public interface ITask {

    /**
     * 任务执行的方法
     */
    void work();

    /**
     * 返回任务名称,用于后续拓展日志或统计用
     * @return
     */
    String getTaskName();
}

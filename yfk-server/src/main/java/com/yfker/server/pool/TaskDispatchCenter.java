package com.yfker.server.pool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @description: 任务调度中心
 * @author: lijiayu
 * @date: 2020-06-08 19:07
 **/
@Component
@Slf4j
public class TaskDispatchCenter {

    @Autowired
    ThreadPool threadPool;

    /**
     * 定时任务线程池
     */
    static ScheduledExecutorService pool;

    /**
     * 默认核心线程的个数为2, 调度中心只是简单的记录任务的周期，不是执行者，所以线程不用太多
     */
    private static final int DEFAULT_CORE_POOL_SIZE = 2;

    /**
     * 缓存所有的任务，用于刷新机制
     */
    private static Map<String, TaskProducer> cacheTasks = new ConcurrentHashMap<>();

    /**
     * 初始化线程池
     */
    @PostConstruct
    public void init() {
        log.info("ScheduledExecutorService init========================================");
        pool = new ScheduledThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, r -> new Thread(r,
                "TaskDispatchCenter"));
    }

    /**
     * 注册即刻执行的任务,该任务不会加入周期性的线程池
     * @param task
     */
    public void registerTask(ITask task) {
        threadPool.execute(task);
    }

    /**
     * 注册周期性任务， 无执行次数限制
     * @param initialDelay the time to delay first execution 延迟首次执行的时间, 0 则为即刻执行 单位秒
     * @param period       the period between successive executions  连续执行之间的时间，周期性时间 单位秒
     * @param task         任务处理的实现类
     */
    public void registerTaskNoLimit(long initialDelay, long period, ITask task) {
        addTaskNoLimit(initialDelay, period, task);
    }

    /**
     * 注册周期性任务， 任务默认执行10次
     * @param key          任务对应的缓存key
     * @param initialDelay the time to delay first execution 延迟首次执行的时间, 0 则为即刻执行 单位秒
     * @param period       the period between successive executions  连续执行之间的时间，周期性时间 单位秒
     * @param execCount    任务执行的次数
     * @param task         任务处理的实现类
     */
    public void registerTask(String key, long initialDelay, long period, short execCount, ITask task) {
        try {
            addTask(key, initialDelay, period, execCount, task);
        } catch (Exception e) {
            log.error("registerTask: " + key, e);
        }
    }

    /**
     * 用于刷新执行次数
     * @param key
     */
    public void refreshTaskExecCount(String key) {
        TaskProducer task = cacheTasks.get(key);
        if (null != task) {
            if (task.getExecuteCount() > 0) {
                task.setExecuteCount((short) 0);
            }
        }
    }

    /**
     * 停止当前任务
     * @param key
     */
    public void toStopTaskByKey(String key) {
        TaskProducer task = cacheTasks.get(key);
        if (null != task) {
            task.stop();
        }
    }

    private void addTask(String key, long initialDelay, long period, short execCount, ITask task) {
        TaskProducer innerTask = new TaskProducer(task, execCount, key);
        if(pool == null){
            log.info("pool is null");
            pool = new ScheduledThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, r -> new Thread(r,
                    "TaskDispatchCenter"));
        }
        ScheduledFuture<?> future = pool.scheduleAtFixedRate(innerTask, initialDelay, period, TimeUnit.SECONDS);
        innerTask.setFuture(future);
        // 将任务加入缓存，用于刷新执行次数
        cacheTasks.put(key, innerTask);
    }

    private void addTaskNoLimit(long initialDelay, long period, ITask task) {
        TaskProducerNoLimit innerTask = new TaskProducerNoLimit(task);
        pool.scheduleAtFixedRate(innerTask, initialDelay, period, TimeUnit.SECONDS);
    }

    /**
     * 内部包装类，包装成一个Runnable并且将任务丢给线程池去执行
     */
    class TaskProducer implements Runnable {

        ITask task;

        /**
         * 执行计划的引用，用于停止任务
         */
        private ScheduledFuture<?> future;

        /**
         * 该任务对应的缓存key
         */
        private String cacheKey;

        /**
         * 保证执行次数可见性，对外提供刷新次数的机制
         */
        private volatile short executeCount;

        /**
         * 执行总次数
         */
        private short execTotalCount;

        public TaskProducer(ITask task, short execTotalCount, String cacheKey) {
            this.task = task;
            this.cacheKey = cacheKey;
            this.execTotalCount = execTotalCount;
            if (execTotalCount <= 0) {
                this.execTotalCount = 10;
            }
        }

        @Override
        public void run() {
            // 判断执行次数，如果超过指定次数，停止任务
            if (++executeCount > execTotalCount) {
                if (null != future) {
                    // 停止任务
                    future.cancel(true);
                    // 从缓存任务池中移除该任务
                    cacheTasks.remove(cacheKey);
                }
            } else {
//                log.info(task.getTaskName() + " is running count:" + this.executeCount);
                threadPool.execute(task);
            }
        }

        public void setFuture(ScheduledFuture<?> future) {
            this.future = future;
        }

        /**
         * 提供可以刷新次数的方法
         * @param executeCount
         */
        public void setExecuteCount(final short executeCount) {
            this.executeCount = executeCount;
        }

        /**
         * 提供可以停止的方法
         */
        public void stop() {
            this.execTotalCount = 0;
        }

        public short getExecuteCount() {
            return this.executeCount;
        }
    }

    /**
     * 内部包装类，包装成一个Runnable并且将任务丢给线程池去执行, 没有执行次数的限制
     */
    class TaskProducerNoLimit implements Runnable {

        ITask task;


        public TaskProducerNoLimit(ITask task) {
            this.task = task;
        }

        @Override
        public void run() {
            threadPool.execute(task);
        }

    }

    /**
     * 获取正在执行的线程数
     * @return
     */
    public int getActiveCount() {
        return threadPool.getActiveCount();
    }

    public void setMaxThreadCount(int threadCount) {
        threadPool.setMaxThreadCount(threadCount);
    }
}

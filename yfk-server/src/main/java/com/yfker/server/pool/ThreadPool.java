package com.yfker.server.pool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @description: 处理任务的线程池模式，用于执行任务
 * @author: lijiayu
 * @date: 2020-05-15 11:04
 **/
@Component
@Slf4j
public class ThreadPool {

    /**
     * 工作线程池
     */
    private static ThreadPoolExecutor threadPool;

    /**
     * 用于缓存被线程池拒绝的任务，等待线程池空闲状态时重新加入线程池
     */
    private static BlockingQueue<Runnable> waitingTaskBlockingQueue = new LinkedBlockingDeque<>();

    /**
     * 用于线程池队列满了的时候，阻塞任务
     */
    private static Thread blockThread;

    /**
     * 标识线程池队列是否已满, 必须使用 volatile 保持可见性
     */
    private static volatile boolean queueIsFull = false;

    /**
     * 默认核心线程数 默认 5
     */
    private int corePoolSize = 5;

    /**
     * 默认最大线程数 默认 30
     */
    private int maximumPoolSize = 30;

    /**
     * 默认缓存队列大小，缓存队列满了之后才会开启非核心线程处理任务 默认 10
     */
    private int cacheTaskQueueSize = 10;

    /**
     * 默认非核心线程的存活时间 单位秒
     */
    private long keepAliveTime = 30L;

    /**
     * 初始化线程池
     */
    @PostConstruct
    public void init() {
        log.info("the EgcThreadPool init is beginning ");
        // 实例化线程池
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(cacheTaskQueueSize),
                new RetryPolicyHandler(waitingTaskBlockingQueue));
        // 处理被线程拒绝的任务，重新丢回到线程池
        new WorkRetryThread().start();
        log.info("the EgcThreadPool init has been completed");
    }

    /**
     * 对外提供动态扩容线程池的大小
     *
     * @param threadCount
     */
    public void setMaxThreadCount(int threadCount) {
        log.error("the EgcThreadPool init is beginning ");
        if (threadCount > maximumPoolSize) {
            threadPool.setMaximumPoolSize(threadCount);
        }
        log.error("the EgcThreadPool init has been completed");
    }

    /**
     * 对外提供接受处理任务的入口
     *
     * @param task
     */
    public void execute(ITask task) {
        try {
            threadPool.execute(new WorkExecutor(task));
        } catch (Exception e) {
            System.out.println("TaskReceiverThread exception：" + e.getMessage());
        }
    }

    /**
     * 当前活跃的线程数
     *
     * @return
     */
    public int getActiveCount() {
        return threadPool.getActiveCount();
    }

    /**
     * 执行接受到的任务
     */
    private class WorkExecutor implements Runnable {

        private ITask task;

        public WorkExecutor(ITask task) {
            this.task = task;
        }

        @Override
        public void run() {
            try {
                long curTimestamp = System.currentTimeMillis();
                task.work();
//                log.info("the task [" + task.getTaskName() + "] elapsed time:" + (System.currentTimeMillis() - curTimestamp));
            } catch (Exception e) {
                // 因为任务都是 schedule 的，所以不考虑异常情况下的重试机制，仅仅只做日志打印
                log.error("WorkExecutor exception：" + e.getMessage(), e);
            } finally {
                // 如果线程池的队列空闲了并且线程池是满的  释放锁
                if (threadPool.getQueue().size() == 0 && queueIsFull) {
                    queueIsFull = false;
                    LockSupport.unpark(blockThread);
                }
            }
        }
    }

    /**
     * 处理被线程拒绝的任务，重新丢回到线程池
     */
    private class WorkRetryThread extends Thread {
        @Override
        public void run() {
            for (; ; ) {
                try {
                    // 如果线程池的队列满了
                    if (threadPool.getQueue().size() == cacheTaskQueueSize) {
                        queueIsFull = true;
                        LockSupport.park(blockThread = Thread.currentThread()); // 阻塞当前线程
                    }
                    Runnable task = waitingTaskBlockingQueue.take();
                    threadPool.execute(task);
                } catch (Exception e) {
                    log.error("WorkRetryThread exception：" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 拒绝策略， 把线程池拒绝的任务，丢到另一个无界队列等待处理
     */
    private class RetryPolicyHandler implements RejectedExecutionHandler {

        private BlockingQueue<Runnable> waitingTaskBlockingQueue;

        public RetryPolicyHandler(BlockingQueue<Runnable> waitingTaskQueue) {
            this.waitingTaskBlockingQueue = waitingTaskQueue;
        }

        @Override
        public void rejectedExecution(Runnable task, ThreadPoolExecutor pool) {
            try {
                waitingTaskBlockingQueue.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

package xyz.xuminghai.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 2023/10/3 17:07 星期二<br/>
 * ThreadPoolExecutor还可以安排命令在给定的延迟后运行，或定期执行。
 * 当需要多个工作线程，或者需要ThreadPoolExecutor （此类扩展）的额外灵活性或功能时，此类比Timer更可取。
 * 延迟任务在启用后立即执行，但对于启用后何时开始没有任何实时保证。
 * 安排在完全相同的执行时间的任务按照先进先出 (FIFO) 的提交顺序启用。
 *
 * @author xuMingHai
 */
public class ScheduledThreadPoolExecutorDemo01 {


    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledThreadPoolExecutorDemo01.class);

    private static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(5);

    public static void main(String[] args) {
        // 延迟启动
        SCHEDULED_THREAD_POOL_EXECUTOR.schedule(() -> LOGGER.info("1"), 1L, TimeUnit.SECONDS);
        SCHEDULED_THREAD_POOL_EXECUTOR.schedule(() -> LOGGER.info("2"), 2L, TimeUnit.SECONDS);
        SCHEDULED_THREAD_POOL_EXECUTOR.schedule(() -> LOGGER.info("3"), 3L, TimeUnit.SECONDS);
        SCHEDULED_THREAD_POOL_EXECUTOR.schedule(() -> LOGGER.info("4"), 4L, TimeUnit.SECONDS);
        SCHEDULED_THREAD_POOL_EXECUTOR.schedule(() -> LOGGER.info("5"), 5L, TimeUnit.SECONDS);

        // 任务完成时关闭
        SCHEDULED_THREAD_POOL_EXECUTOR.shutdown();
    }

}

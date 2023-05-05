package xyz.xuminghai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 2023/5/1 23:35 星期一<br/>
 *
 * <h1>CountDownLatch示例1</h1>
 * 允许一个或多个线程等待，直到其他线程中执行的一组操作完成
 * 这是一种一次性现象——无法重置计数。
 *
 * @author xuMingHai
 */
public class CountDownLatchDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CountDownLatchDemo01.class);

    /**
     * 工作线程数量
     */
    private static final int WORKER_THREAD_NUMBER = 3;

    /**
     * 多个工作线程执行完毕后，再执行主线程
     */
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(WORKER_THREAD_NUMBER);

    public static void main(String[] args) {

        for (int i = 0; i < WORKER_THREAD_NUMBER; i++) {
            new Thread(() -> {
                LOGGER.info("Ok");
                COUNT_DOWN_LATCH.countDown();
            }).start();
        }

        try {
            // 等待统计计数为0
            COUNT_DOWN_LATCH.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Ok");
    }

}

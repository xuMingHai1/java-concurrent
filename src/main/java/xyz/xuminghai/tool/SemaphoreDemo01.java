package xyz.xuminghai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 2023/5/4 16:00 星期四<br/>
 * <h1>Semaphore示例01</h1>
 * 信号量维护一组许可。如果有必要，每个acquire块直到许可可用，然后获取它。
 * 每个release都会添加一个许可，可能会释放一个阻塞的获取者。
 *
 * @author xuMingHai
 */
public class SemaphoreDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SemaphoreDemo01.class);

    /**
     * 使用非公平策略，许可为3的信号量
     */
    private static final Semaphore SEMAPHORE = new Semaphore(3);

    public static void main(String[] args) {

        // 100个线程只有3个线程获取许可
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                // 获取许可
                try {
                    SEMAPHORE.acquire();
                    // 获取到许可后，休眠10毫秒
                    TimeUnit.MILLISECONDS.sleep(10L);
                    LOGGER.info("------ 执行完毕 --------");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 释放许可
                SEMAPHORE.release();
            }).start();
        }

    }

}

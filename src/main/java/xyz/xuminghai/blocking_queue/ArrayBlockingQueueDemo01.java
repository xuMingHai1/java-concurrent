package xyz.xuminghai.blocking_queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 2023/5/7 21:45 星期日<br/>
 * <h1>ArrayBlockingQueue示例01</h1>
 * 由数组实现的阻塞队列，该队列对元素进行 FIFO（先进先出）排序。队列的头部是队列中时间最长的元素。队列的尾部是在队列中时间最短的元素。
 * 新元素插入队列尾部，队列检索操作获取队列头部元素。
 *
 * @author xuMingHai
 */
public class ArrayBlockingQueueDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayBlockingQueueDemo01.class);

    /**
     * 生产消费模型
     */
    private static final ArrayBlockingQueue<Integer> ARRAY_BLOCKING_QUEUE = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) {
        // 消费者
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    LOGGER.info("item = {}", ARRAY_BLOCKING_QUEUE.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // 生产者
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ARRAY_BLOCKING_QUEUE.add(i);
        }
    }

}

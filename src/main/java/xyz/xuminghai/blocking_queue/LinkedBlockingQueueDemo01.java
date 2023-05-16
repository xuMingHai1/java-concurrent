package xyz.xuminghai.blocking_queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2023/5/8 13:59 星期一<br/>
 * <h1>LinkedBlockingQueue示例01</h1>
 * 基于链接节点的可选边界阻塞队列。该队列对元素进行 FIFO（先进先出）排序。
 * 队列的头部是队列中时间最长的元素。队列的尾部是在队列中时间最短的元素。
 * 新元素插入队列尾部，队列检索操作获取队列头部元素。
 * 链接队列通常比基于数组的队列具有更高的吞吐量，但在大多数并发应用程序中性能更不可预测
 *
 * @author xuMingHai
 */
public class LinkedBlockingQueueDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkedBlockingQueueDemo01.class);

    private static final LinkedBlockingQueue<Integer> LINKED_BLOCKING_QUEUE = new LinkedBlockingQueue<>();

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    public static void main(String[] args) {
        // 多生产者
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                int j = (int) (Math.random() * 50) + 51;
                for (int k = 0; k < j; k++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    LINKED_BLOCKING_QUEUE.add(k);
                }
                LOGGER.info("生产完成。。。。。。。");
                ATOMIC_INTEGER.getAndIncrement();
            }).start();
        }

        // 单消费者
        while (ATOMIC_INTEGER.get() < 3) {
            try {
                LOGGER.info("item = {}", LINKED_BLOCKING_QUEUE.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}

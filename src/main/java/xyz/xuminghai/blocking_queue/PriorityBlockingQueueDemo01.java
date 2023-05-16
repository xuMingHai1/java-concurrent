package xyz.xuminghai.blocking_queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 2023/5/8 14:48 星期一<br/>
 * <h1>PriorityBlockingQueue示例01</h1>
 * 一个无界阻塞队列，它使用与类PriorityQueue相同的排序规则并提供阻塞检索操作。
 * 虽然此队列在逻辑上是无界的，但由于资源耗尽（导致OutOfMemoryError ），
 * 尝试添加可能会失败。此类不允许null元素。依赖自然排序的优先级队列也不允许插入不可比较的对象（这样做会导致ClassCastException ）。
 *
 * @author xuMingHai
 */
public class PriorityBlockingQueueDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PriorityBlockingQueueDemo01.class);

    private static final PriorityBlockingQueue<Integer> PRIORITY_BLOCKING_QUEUE = new PriorityBlockingQueue<>();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            PRIORITY_BLOCKING_QUEUE.add((int) (Math.random() * 10));
        }
        // 在插入时没有按照优先级进行排序
        LOGGER.info("PRIORITY_BLOCKING_QUEUE = {}", PRIORITY_BLOCKING_QUEUE);
        for (int i = 0; i < 10; i++) {
            try {
                LOGGER.info("{}", PRIORITY_BLOCKING_QUEUE.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

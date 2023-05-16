package xyz.xuminghai.blocking_queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SynchronousQueue;

/**
 * 2023/5/15 14:26 星期一<br/>
 * <h1>SynchronousQueue示例01</h1>
 * 一个阻塞队列，其中每个插入操作都必须等待另一个线程的相应删除操作，
 * 反之亦然。同步队列没有任何内部容量，甚至没有一个容量。
 * 您无法peek同步队列，因为元素仅在您尝试删除它时才存在；
 * 你不能插入一个元素（使用任何方法），除非另一个线程试图删除它；
 * 你不能迭代，因为没有什么可以迭代的。队列的头部是第一个排队的插入线程试图添加到队列中的元素；
 * 如果没有这样的排队线程，则没有元素可用于删除， poll()将返回null 。
 * 对于其他Collection方法（例如contains ）， SynchronousQueue充当空集合。
 * 此队列不允许null元素
 * 它们非常适合切换设计，在这种设计中，一个线程中运行的对象必须与另一个线程中运行的对象同步，以便将一些信息、事件或任务交给它。
 *
 * @author xuMingHai
 */
public class SynchronousQueueDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronousQueueDemo01.class);

    /**
     * 非公平访问策略
     */
    private static final SynchronousQueue<String> SYNCHRONOUS_QUEUE = new SynchronousQueue<>();

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                LOGGER.info("获取对象");
                String s = SYNCHRONOUS_QUEUE.take();
                LOGGER.info("s = {}", s);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        try {
            SYNCHRONOUS_QUEUE.put("hello");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("OK");

    }
}

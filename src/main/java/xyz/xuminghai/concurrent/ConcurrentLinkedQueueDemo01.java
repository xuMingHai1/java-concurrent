package xyz.xuminghai.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 2023/5/17 13:05 星期三<br/>
 * <h1>ConcurrentLinkedQueue示例01</h1>
 * 基于链接节点的无界线程安全队列。该队列对元素进行 FIFO（先进先出）排序。
 * 队列的头部是队列中时间最长的元素。队列的尾部是在队列中时间最短的元素。
 * 新元素插入队列尾部，队列检索操作获取队列头部元素。
 * 当许多线程将共享对公共集合的访问时， ConcurrentLinkedQueue是一个合适的选择。
 * 与大多数其他并发集合实现一样，此类不允许使用null元素。
 * Concurrent集合不提供阻塞功能
 *
 * @author xuMingHai
 */
public class ConcurrentLinkedQueueDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentLinkedQueueDemo01.class);

    /**
     * 一个无界的非阻塞并发队列
     */
    private static final ConcurrentLinkedQueue<String> CONCURRENT_LINKED_QUEUE = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {

        // 多线程并发修改
        new Thread(() -> {
            String s = CONCURRENT_LINKED_QUEUE.poll();
            LOGGER.info("s = {}", s);
        }).start();

        new Thread(() -> {
            String s = CONCURRENT_LINKED_QUEUE.poll();
            LOGGER.info("s = {}", s);
        }).start();

        CONCURRENT_LINKED_QUEUE.add("hello");

        // 弱一致性的迭代器
        for (String s : CONCURRENT_LINKED_QUEUE) {
            LOGGER.info("s = {}", s);
        }

    }

}

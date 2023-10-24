package xyz.xuminghai.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 2023/5/17 13:19 星期三<br/>
 * <h1>ConcurrentLinkedDeque示例01</h1>
 * 基于链接节点的无界并发双端队列。
 * 并发插入、删除和访问操作跨多个线程安全执行。
 * 当许多线程将共享对公共集合的访问时， ConcurrentLinkedDeque是一个合适的选择。
 * 与大多数其他并发集合实现一样，此类不允许使用null元素。
 * 迭代器和拆分器是弱一致的
 *
 * @author xuMingHai
 */
public class ConcurrentLinkedDequeDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentLinkedDequeDemo01.class);

    /**
     * 基于链表的无界非阻塞并发双端队列
     */
    private static final ConcurrentLinkedDeque<String> CONCURRENT_LINKED_DEQUE = new ConcurrentLinkedDeque<>();

    public static void main(String[] args) {

        CONCURRENT_LINKED_DEQUE.add("hello");

        new Thread(() -> {
            CONCURRENT_LINKED_DEQUE.addLast("world");
            CONCURRENT_LINKED_DEQUE.addFirst("Hi");
        }).start();

        new Thread(() -> {
            String firstItem = CONCURRENT_LINKED_DEQUE.pollFirst();
            String lastItem = CONCURRENT_LINKED_DEQUE.pollLast();
            LOGGER.info("firstItem = {}", firstItem);
            LOGGER.info("lastItem = {}", lastItem);
        }).start();

        // 弱一致性
        for (String s : CONCURRENT_LINKED_DEQUE) {
            LOGGER.info("s = {}", s);
        }
    }

}

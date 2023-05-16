package xyz.xuminghai.blocking_queue.blocking_deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 2023/5/16 22:04 星期二<br/>
 * <h1>LinkedBlockingDeque示例01</h1>
 * 一个Deque ，它还支持阻塞操作，在检索元素时等待双端队列变为非空，并在存储元素时等待双端队列中的空间可用。
 * 基于链接节点的可选边界阻塞双端队列。
 * 可选的容量限制构造函数参数用作防止过度扩展的方法。容量（如果未指定）等于Integer.MAX_VALUE 。链接节点是在每次插入时动态创建的，除非这会使双端队列超出容量。
 *
 * @author xuMingHai
 */
public class LinkedBlockingDequeDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkedBlockingDequeDemo01.class);

    /**
     * 固定容量的双端阻塞队列
     */
    private static final LinkedBlockingDeque<String> LINKED_BLOCKING_DEQUE = new LinkedBlockingDeque<>(10);


    public static void main(String[] args) {

        // 获取第一个元素
        new Thread(() -> {
            String s;
            try {
                s = LINKED_BLOCKING_DEQUE.takeFirst();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("First = {}", s);
        }).start();

        // 获取最后一个元素
        new Thread(() -> {
            String s;
            try {
                s = LINKED_BLOCKING_DEQUE.takeLast();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("Last = {}", s);
        }).start();

        try {
            LINKED_BLOCKING_DEQUE.put("hello");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}

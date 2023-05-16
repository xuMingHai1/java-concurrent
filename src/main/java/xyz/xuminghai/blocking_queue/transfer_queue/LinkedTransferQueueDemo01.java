package xyz.xuminghai.blocking_queue.transfer_queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

/**
 * 2023/5/16 1:21 星期二<br/>
 * <h1>LinkedTransferQueue示例01</h1>
 * 传输队列，生产者可以在其中等待消费者接收元素。
 * TransferQueue在消息传递应用程序中可能很有用，在这些应用程序中，生产者有时（使用方法transfer ）等待消费者调用take或poll接收元素，
 * 而在其他时候将元素排队（通过方法put ）而不等待接收。 tryTransfer的非阻塞和超时版本也可用。
 * 还可以通过hasWaitingConsumer查询TransferQueue是否有任何线程在等待项目，这与peek操作相反。<br/>
 * 基于链接节点的无界TransferQueue 。该队列根据任何给定的生产者对元素进行 FIFO（先进先出）排序。
 * 队列的头部是某个生产者在队列中时间最长的元素。队列的尾部是某个生产者在队列中时间最短的元素。
 *
 * @author xuMingHai
 */
public class LinkedTransferQueueDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkedTransferQueueDemo01.class);

    /**
     * 一个无界的传输队列
     */
    private static final LinkedTransferQueue<String> LINKED_TRANSFER_QUEUE = new LinkedTransferQueue<>();


    public static void main(String[] args) {

        new Thread(() -> {
            LOGGER.info("开始传输，等待消费者获取");
            try {
                LINKED_TRANSFER_QUEUE.transfer("hello");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("传输完毕");
        }).start();

        LOGGER.info("休眠1秒");
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            LOGGER.info("数据 = {}", LINKED_TRANSFER_QUEUE.take());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}

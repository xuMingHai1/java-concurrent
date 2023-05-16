package xyz.xuminghai.blocking_queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.StringJoiner;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 2023/5/15 14:42 星期一<br/>
 * <h1>DelayQueue示例01</h1>
 * Delayed元素的无界阻塞队列，其中的元素只有在其延迟到期时才能被获取。
 * 队列的头部是延迟在过去最远到期的Delayed元素。如果没有延迟到期，则没有 head 并且poll将返回null 。
 * 当元素的getDelay(TimeUnit.NANOSECONDS)方法返回小于或等于零的值时，就会过期。
 * 即使无法使用take或poll删除未过期的元素，它们也会被视为普通元素。
 * 例如， size方法返回过期和未过期元素的计数。此队列不允许空元素
 *
 * @author xuMingHai
 */
public class DelayQueueDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DelayQueueDemo01.class);

    private static final DelayQueue<DelayedItem> DELAY_QUEUE = new DelayQueue<>();


    public static void main(String[] args) {
        DELAY_QUEUE.put(DelayedItem.delayedItemFactory(5));
        DELAY_QUEUE.put(DelayedItem.delayedItemFactory(3));
        DELAY_QUEUE.put(DelayedItem.delayedItemFactory(8));
        DELAY_QUEUE.put(DelayedItem.delayedItemFactory(9));
        DELAY_QUEUE.put(DelayedItem.delayedItemFactory(14));

        for (int i = 0; i < 5; i++) {
            DelayedItem delayedItem;
            try {
                delayedItem = DELAY_QUEUE.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("delayedItem = {}", delayedItem);
        }

    }

    /**
     * 延迟对象
     */
    private static class DelayedItem implements Delayed {

        private final long expireTime;

        private DelayedItem(int seconds) {
            this.expireTime = System.currentTimeMillis() + seconds * 1000L;
        }

        public static DelayedItem delayedItemFactory(int seconds) {
            return new DelayedItem(seconds);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expireTime - System.currentTimeMillis(),
                    TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            long diff;
            if (o instanceof DelayedItem) {
                DelayedItem other = (DelayedItem) o;
                diff = this.expireTime - other.expireTime;
            } else {
                diff = this.getDelay(TimeUnit.MILLISECONDS) -
                        o.getDelay(TimeUnit.MILLISECONDS);
            }
            return (diff == 0) ? 0 : (diff < 0 ? -1 : 1);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", DelayedItem.class.getSimpleName() + "[", "]")
                    .add("expireTime=" + expireTime)
                    .add("expireTime=" + Instant.ofEpochMilli(expireTime))
                    .toString();
        }
    }

}

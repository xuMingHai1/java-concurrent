package xyz.xuminghai.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.LongAccumulator;

/**
 * 2023/3/22 15:37 星期三<br/>
 *
 * <h1>LongAccumulator示例</h1>
 * 与longAdder相比，拥有基础值和一个二元操作符，left为base，right为进行计算的参数
 *
 * @author xuMingHai
 */
public class LongAccumulatorDemo {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LongAccumulatorDemo.class);

    public static void main(String[] args) {
        // 减少操作
        final LongAccumulator longAccumulator = new LongAccumulator((left, right) -> left - right, 300L);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                longAccumulator.accumulate(1L);
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                longAccumulator.accumulate(1L);
            }
        }, "t2");

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                longAccumulator.accumulate(1L);
            }
        }, "t3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("longAccumulator = {}", longAccumulator);

    }

}

package xyz.xuminghai.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.LongAdder;

/**
 * 2023/3/22 15:03 星期三<br/>
 *
 * <h1>LongAdder示例</h1>
 * 使用一个base变量和一组缓存行填充的base变量来维护对base变量的修改，
 * 当多线程并发修改时，只会修改数组内对应的变量，获取值时进行最终求和，
 * 适用于统计信息，而非多线程修改时对变量的精准控制
 *
 * @author xuMingHai
 */
public class LongAdderDemo {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicMarkReferenceDemo.class);

    public static void main(String[] args) {

        // long 类型累加器
        final LongAdder longAdder = new LongAdder();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                longAdder.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                longAdder.increment();
            }
        }, "t2");

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                longAdder.decrement();
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

        LOGGER.info("longAdder = {}", longAdder);
    }

}

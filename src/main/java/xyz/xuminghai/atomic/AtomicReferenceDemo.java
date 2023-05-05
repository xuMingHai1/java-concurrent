package xyz.xuminghai.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 2023/3/20 13:58 星期一<br/>
 *
 * <h1>AtomicReference示例</h1>
 * 使用原子引用模拟原子整型操作
 *
 * @author xuMingHai
 */
public class AtomicReferenceDemo {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicReferenceDemo.class);


    public static void main(String[] args) {

        final AtomicReference<Integer> atomicReference = new AtomicReference<>(0);

        Thread t1 = new Thread(() -> atomicReference.getAndUpdate(i -> i + 1), "t1");
        Thread t2 = new Thread(() -> atomicReference.getAndUpdate(i -> i - 1), "t2");
        Thread t3 = new Thread(() -> atomicReference.getAndUpdate(i -> i + 1), "t3");

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

        LOGGER.info("atomicReference = {}", atomicReference);
    }

}

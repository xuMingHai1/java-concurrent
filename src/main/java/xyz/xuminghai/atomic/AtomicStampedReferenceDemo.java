package xyz.xuminghai.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 2023/3/21 20:33 星期二<br/>
 *
 * <h1>AtomicStampedReference示例</h1>
 * 使用戳来记录修改
 *
 * @author xuMingHai
 */
public class AtomicStampedReferenceDemo {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicStampedReferenceDemo.class);


    public static void main(String[] args) {
        // 将戳与原子整型保持一致，自增时戳自增，自减时戳自减
        final AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(0, 0);

        Thread t1 = new Thread(() -> {
            // 原子自增，同时戳也自增
            for (int i = 0; i < 100; i++) {
                int expectedReference, newReference, expectedStamp, newStamp;
                do {
                    expectedReference = stampedReference.getReference();
                    newReference = expectedReference + 1;
                    expectedStamp = stampedReference.getStamp();
                    newStamp = expectedStamp + 1;
                } while (!stampedReference.compareAndSet(expectedReference, newReference,
                        expectedStamp, newStamp));

            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            // 原子自减，同时戳也自减
            for (int i = 0; i < 100; i++) {
                int expectedReference, newReference, expectedStamp, newStamp;
                do {
                    expectedReference = stampedReference.getReference();
                    newReference = expectedReference - 1;
                    expectedStamp = stampedReference.getStamp();
                    newStamp = expectedStamp - 1;
                } while (!stampedReference.compareAndSet(expectedReference, newReference,
                        expectedStamp, newStamp));

            }
        }, "t2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 打印最后的结果
        LOGGER.info("reference = {}, stamp = {}", stampedReference.getReference(), stampedReference.getStamp());
    }

}

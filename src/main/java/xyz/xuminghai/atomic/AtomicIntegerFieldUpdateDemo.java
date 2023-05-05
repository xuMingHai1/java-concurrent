package xyz.xuminghai.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 2023/3/14 14:07 星期二<br/>
 *
 * <h1>AtomicIntegerFieldUpdater示例</h1>
 * 根据 Java 语言访问控制，调用者无法访问该字段，则出现基于反射的嵌套异常
 *
 * @author xuMingHai
 */
public class AtomicIntegerFieldUpdateDemo {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicIntegerFieldUpdateDemo.class);


    public static void main(String[] args) {

        final AtomicIntegerFieldUpdater<VolatileInteger> fieldUpdater = AtomicIntegerFieldUpdater
                .newUpdater(VolatileInteger.class, "i");
        final VolatileInteger volatileInteger = new VolatileInteger();

        Thread t1 = new Thread(() -> fieldUpdater.getAndIncrement(volatileInteger), "t1");

        Thread t2 = new Thread(() -> fieldUpdater.getAndDecrement(volatileInteger), "t2");

        Thread t3 = new Thread(() -> fieldUpdater.getAndIncrement(volatileInteger), "t3");

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

        LOGGER.info("volatileInteger = {}", volatileInteger);

    }

}

class VolatileInteger {
    volatile int i;

    @Override
    public String toString() {
        return new StringJoiner(", ", VolatileInteger.class.getSimpleName() + "[", "]")
                .add("i=" + i)
                .toString();
    }
}
package xyz.xuminghai.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.function.IntUnaryOperator;

/**
 * 2022/3/7 11:25 星期一<br/>
 * <h1>AtomicInteger和AtomicLong原理分析</h1>
 * AtomicInteger本质使用的是{@link Unsafe}，利用CAS机制，比较期望值和实际值，成功的话则将实际值替换为更新值
 *
 * @author xuMingHai
 */
public class AtomicIntegerAndAtomicLongDemo02 {


    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicIntegerAndAtomicLongDemo02.class);


    public static void main(String[] args) {

        // 多个线程共享的资源，初始值为 1
        final SimpleAtomicInteger simpleAtomicInteger = new SimpleAtomicInteger(1);

        // 使用多个线程测试自定义的原子类
        final Thread t1 = new Thread(() -> {
            // 第一个线程自减
            for (int i = 0; i < 1_000; i++) {
                simpleAtomicInteger.getAndUpdate(operand -> --operand);
            }
            LOGGER.info("t1 线程的结果 = {}", simpleAtomicInteger.get());
        }, "t1");

        final Thread t2 = new Thread(() -> {
            // 第二个线程自增
            for (int i = 0; i < 500; i++) {
                simpleAtomicInteger.getAndUpdate(operand -> ++operand);
            }
            LOGGER.info("t2 线程的结果 = {}", simpleAtomicInteger.get());
        }, "t2");

        final Thread t3 = new Thread(() -> {
            // 第三个线程自增
            for (int i = 0; i < 500; i++) {
                simpleAtomicInteger.getAndUpdate(operand -> ++operand);
            }
            LOGGER.info("t3 线程的结果 = {}", simpleAtomicInteger.get());
        }, "t3");

        // 启动三个线程
        t1.start();
        t2.start();
        t3.start();

        // 等待这个三个线程运行结束
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打印最后的结果
        LOGGER.info("最终的结果 = {}", simpleAtomicInteger.get());

    }

    /**
     * 简单的原子整型实现类
     */
    private static class SimpleAtomicInteger {
        /**
         * 用于执行不安全方法的集合
         */
        private static final Unsafe UNSAFE;

        /**
         * value字段的偏移量，同一类的不同实例的字段偏移量是相同的
         */
        private static final long VALUE_OFFSET;

        /*
                    使用unsafe获取value字段的偏移量
                 */
        static {
            try {
                final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                UNSAFE = (Unsafe) theUnsafe.get(null);
                theUnsafe.setAccessible(false);

                VALUE_OFFSET = UNSAFE.objectFieldOffset(SimpleAtomicInteger.class.getDeclaredField("value"));
            } catch (Exception ex) {
                throw new Error(ex);
            }
        }

        private volatile int value;

        public SimpleAtomicInteger(int value) {
            set(value);
        }

        /**
         * 获取当前值，因为value是使用volatile修饰的
         *
         * @return value
         */
        public int get() {
            return value;
        }

        /**
         * 将当前值设置为新的值
         *
         * @param newValue 新的值
         */
        public void set(int newValue) {
            value = newValue;
        }

        /**
         * 将预期值和实际值作比较，如果没有线程已经修改了，那么将值改为要更新的值
         *
         * @param expect 预期值
         * @param update 更新值
         * @return true表示更新成功，false更新失败
         */
        public boolean completeAndSet(int expect, int update) {
            return UNSAFE.compareAndSwapInt(this, VALUE_OFFSET, expect, update);
        }

        /**
         * 获取并更新原子
         *
         * @param intUnaryOperator 返回要更新的值
         */
        public void getAndUpdate(IntUnaryOperator intUnaryOperator) {
            // 期望值，要更改的值
            int prev, next;
            int i = 0;
            // 循环修改
            do {
                LOGGER.info("第{}次CAS", ++i);
                // 获取最新值
                prev = get();
                // 得到要更改的值
                next = intUnaryOperator.applyAsInt(prev);
                // CAS设置值
            } while (!completeAndSet(prev, next));

        }

    }

}

package xyz.xuminghai.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2022/3/4 5:14 星期五<br/>
 * <h1>volatile示例</h1>
 * volatile关键字的作用：
 * <ul>
 *     <li>内存可见性，基于内存可见性，就可以实现一个线程写，多个线程读的原子性</li>
 *     <li>防止指令重排序</li>
 * </ul>
 * 更加详细的测试请参见 concurrency-test模块的VolatileTest
 * @author xuMingHai
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
public class VolatileDemo05 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VolatileDemo05.class);


    public static void main(String[] args) {

        // 原子性测试
        Demo1.test();

        // 防止指令重排序测试
        Demo2.getInstance().printMessage();

    }


    /**
     * 原子性测试（一个线程写，多个线程读）
     */
    private static class Demo1 {

        /**
         * 多个线程的共享资源，添加volatile确保可见性
         */
        private volatile static long value;

        public static void test() {
            // 线程1写
            final Thread t1 = new Thread(() -> value = 10);

            // 线程2读
            final Thread t2 = new Thread(() -> LOGGER.info("value = {}", value));

            // 线程3读
            final Thread t3 = new Thread(() -> LOGGER.info("value = {}", value));

            // 启动线程
            t1.start();
            t2.start();
            t3.start();

            // 主线程读
            LOGGER.info("value = {}", value);

        }


    }

    /**
     * 防止指令重排序
     */
    private static class Demo2 {

        /**
         * 添加volatile确保在赋值的时候不会先获取值后再赋值
         */
        private volatile static Instance instance;

        public static Instance getInstance() {
            // 先不加锁判断是否为null
            if (instance == null) {
                // 获取锁之后再次判断，如果没有其他线程已经创建了实例对象就创建
                synchronized (Demo2.class) {
                    if (instance == null) {
                        instance = new Instance();
                    }
                }
            }
            return instance;
        }

        private static class Instance {

            public void printMessage() {
                LOGGER.info("Hello Word!");
            }
        }

    }


}

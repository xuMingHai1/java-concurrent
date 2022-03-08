package xyz.xuminghai.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2022/3/2 18:10 星期三<br/>
 * <h1>synchronized示例</h1>
 * synchronized原理是给对象加了一把锁，他通常使用在实例方法、静态方法或同步块内。<br/>
 * 当多个线程运行同步代码时，会先获取这个对象锁，获取锁成功，则会执行。<br/>
 * 如果获取锁失败，则会进入等待对列中阻塞，当获取到锁的线程执行完同步块内的代码，他会唤醒在队列中等待的线程。<br/>
 * 至于唤醒哪一个线程，这是随机的。
 *
 * @author xuMingHai
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
public class SynchronizedDemo03 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedDemo03.class);

    /**
     * 自增或自减的次数
     */
    public static final int FREQUENCY = 1_000_000;

    /**
     * 是否加锁，加锁为true不加锁为false
     */
    private final boolean isSynchronized;

    public SynchronizedDemo03(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }


    public static void main(String[] args) {

        // 无锁的运行
        new SynchronizedDemo03(false).run();

        // 加锁的运行
        new SynchronizedDemo03(true).run();

    }


    /**
     * 运行示例
     */
    public void run() {
        // 多线程共享的资源
        final Resource resource = new Resource();


        // 创建多个线程去使用共享资源，请测试如果我们不加锁和加锁的区别
        // 创建线程t1
        final Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                // t1线程自增
                for (int i = 0; i < FREQUENCY; i++) {
                    if (isSynchronized) {
                        resource.syncIncrement();
                    } else {
                        resource.increment();
                    }
                }
            }
        };

        // 创建线程t2
        final Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                // t1线程自减
                for (int i = 0; i < FREQUENCY; i++) {
                    if (isSynchronized) {
                        resource.syncDecrement();
                    } else {
                        resource.decrement();
                    }
                }
            }
        };

        // 启动两个线程
        t1.start();
        t2.start();


        // 开始计时运行时长
        final long startTimeMillis = System.currentTimeMillis();

        if (isSynchronized) {
            LOGGER.info("加锁的运行");
        } else {
            LOGGER.info("无锁的运行");
        }

        // 等待两个线程运行结束
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 获取最后的结果，请区分加锁和不加锁的区别
            LOGGER.info("i = {}", Resource.getValue());
            LOGGER.info("运行时长：{}（毫秒）", System.currentTimeMillis() - startTimeMillis);
            // 初始化结果
            Resource.i = 0;
        }
    }

    /**
     * 多线程要访问的资源
     */
    private static class Resource {
        private static int i;

        /**
         * 加锁的对变量自增
         */
        public void syncIncrement() {
            // 这个同步块给实例对象加锁
            synchronized (this) {
                i++;
            }
        }

        /**
         * 无锁的变量自增
         */
        public void increment() {
            i++;
        }

        /**
         * 加锁的对变量自减，给这个方法加上实例对象的锁
         */
        public synchronized void syncDecrement() {
            i--;
        }

        /**
         * 无锁的变量自减
         */
        public void decrement() {
            i--;
        }

        /**
         * 获取当前资源的值，请注意：在这里加锁是给这个类对象加锁。
         * 而前两个加的锁是这个类的实例对象，因为锁是不一样的，所以在我们调用这个方法的时候，他并不会跟前两个方法发生争抢锁的竞争关系
         * @return int i
         */
        private static synchronized int getValue() {
            return i;
        }

    }


}

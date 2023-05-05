package xyz.xuminghai.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 2022/4/5 21:34 星期二<br/>
 * <h1>AtomicBoolean并发锁示例</h1>
 * 使用AtomicBoolean的原子性，实现并发锁
 *
 * @author xuMingHai
 */
public class AtomicBooleanSimpleLockDemo {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicBooleanSimpleLockDemo.class);


    public static void main(String[] args) {

        /*
            为什么需要AtomicBoolean？
            答案是我们需要给boolean变量赋值，so，需要原子boolean
         */

        // 使用原子Boolean实现得简单同步锁
        final SimpleLock simpleLock = new SimpleLock();

        // 多个线程共享的资源
        final StringJoiner stringJoiner = new StringJoiner(",");

        final Thread t1 = new Thread(() -> {
            // 当前线程的名字
            final String name = Thread.currentThread().getName();

            // 获取锁
            simpleLock.lock();
            try {
                LOGGER.info("{} 成功获取锁", name);
                // 获取锁后先休眠
                TimeUnit.MILLISECONDS.sleep(100);
                // 拼接当前线程名
                stringJoiner.add(name);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOGGER.info("{} 开始释放锁", name);
                // 无论是否成功运行完，都要释放锁
                simpleLock.unLock();

            }
        }, "t1");

        final Thread t2 = new Thread(() -> {
            // 当前线程的名字
            final String name = Thread.currentThread().getName();

            // 获取锁
            simpleLock.lock();
            try {
                LOGGER.info("{} 成功获取锁", name);
                // 获取锁后先休眠
                TimeUnit.MILLISECONDS.sleep(200);
                // 拼接当前线程名
                stringJoiner.add(name);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOGGER.info("{} 开始释放锁", name);
                // 无论是否成功运行完，都要释放锁
                simpleLock.unLock();
            }
        }, "t2");

        final Thread t3 = new Thread(() -> {
            // 当前线程的名字
            final String name = Thread.currentThread().getName();

            // 获取锁
            simpleLock.lock();
            try {
                LOGGER.info("{} 成功获取锁", name);
                // 获取锁后先休眠
                TimeUnit.MILLISECONDS.sleep(300);
                // 拼接当前线程名
                stringJoiner.add(name);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOGGER.info("{} 开始释放锁", name);
                // 无论是否成功运行完，都要释放锁
                simpleLock.unLock();
            }
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
        LOGGER.info("结果 = {}", stringJoiner);

    }


    /**
     * 简单的使用原子Boolean实现同步锁
     */
    private static class SimpleLock {
        // 使用原子Boolean来模拟锁，默认锁可用状态
        private final AtomicBoolean atomicBoolean = new AtomicBoolean(true);


        /**
         * 锁方法，引用同一个原子Boolean，条件为真表示锁可用，为假循环等待
         */
        private void lock() {
            // 当已经有线程获取到锁时，进行循环等待释放锁
            while (!atomicBoolean.get()) {
                // 提示当前线程愿意放弃其当前对处理器的使用。
                Thread.yield();
            }

            // 从循环中结束表示，锁已经释放开始争抢锁
            // 尝试获取锁，将锁可用改为false
            if (!atomicBoolean.compareAndSet(true, false)) {
                // 获取锁失败，则进入递归等待下次获取锁
                lock();
            }

            // 成功获得锁，结束方法
        }

        /**
         * 解锁方法，设置原子Boolean为true
         */
        private void unLock() {
            atomicBoolean.set(true);
        }

    }

}

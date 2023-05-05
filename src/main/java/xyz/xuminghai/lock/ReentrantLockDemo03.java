package xyz.xuminghai.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 2023/3/30 20:25 星期四<BR/>
 *
 * <h1>ReentrantLock示例03</h1>
 * ReentrantLock的Condition使用
 *
 * @author xuMingHai
 */
public class ReentrantLockDemo03 {

    /**
     * 可重入锁，已获取到锁的线程可以重复获取锁，默认是非公平锁
     */
    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    /**
     * 关联可重入锁的条件变量，只有在获取到锁时才能使用。
     * 可能会出现虚假唤醒，一般将条件放置于循环中
     */
    private static final Condition CONDITION = REENTRANT_LOCK.newCondition();

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReentrantLockDemo03.class);

    private static int i;

    public static void main(String[] args) {
        // await
        new Thread(() -> {
            REENTRANT_LOCK.lock();
            try {
                // 防止虚假唤醒
                while (i != 0) {
                    try {
                        // 等待时会解锁，被唤醒后，会重新获取锁
                        CONDITION.await();
                    } catch (InterruptedException e) {
                        // 线程被中断
                        throw new RuntimeException(e);
                    }
                }
                // 条件匹配执行操作
                i++;
                LOGGER.info("i = {}", i);
            } finally {
                // 运行结束后唤醒在该条件等待的线程
                CONDITION.signalAll();
                REENTRANT_LOCK.unlock();
            }
        }).start();

        // awaitUninterruptibly
        new Thread(() -> {
            REENTRANT_LOCK.lock();
            try {
                while (i != 1) {
                    CONDITION.awaitUninterruptibly();
                }
                // 条件匹配执行操作
                i++;
                LOGGER.info("i = {}", i);
            } finally {
                CONDITION.signalAll();
                REENTRANT_LOCK.unlock();
            }
        }).start();

        // awaitNanos(nanosTimeout)
        new Thread(() -> {
            // 等待时长
            long nanos = TimeUnit.SECONDS.toNanos(1);
            REENTRANT_LOCK.lock();
            try {
                while (i != 2) {
                    try {
                        if (nanos <= 0L) {
                            LOGGER.info("awaitNanos(nanosTimeout) 获取条件超时");
                            // 结束运行
                            return;
                        }
                        // 等待剩余的时长
                        nanos = CONDITION.awaitNanos(nanos);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 条件匹配执行操作
                i++;
                LOGGER.info("i = {}", i);
            } finally {
                CONDITION.signalAll();
                REENTRANT_LOCK.unlock();
            }
        }).start();

        // await(time, unit)
        new Thread(() -> {
            REENTRANT_LOCK.lock();
            try {
                while (i != 3) {
                    try {
						/*
							个人认为
							awaitNanos(nanosTimeout) 用于等待的总时长
							await(time, unit) 用于等待周期时长
						 */
                        if (!CONDITION.await(1L, TimeUnit.SECONDS)) {
                            LOGGER.info("await(time, unit) 等待周期内没有被唤醒超时");
                            return;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 条件匹配执行操作
                i++;
                LOGGER.info("i = {}", i);
            } finally {
                CONDITION.signalAll();
                REENTRANT_LOCK.unlock();
            }
        }).start();

        // awaitUntil(deadline)
        new Thread(() -> {
            // 最后期限
            Date deadline = new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1));
            REENTRANT_LOCK.lock();
            try {
                while (i != 4) {
                    try {
                        // 规定指定时间之前
                        if (!CONDITION.awaitUntil(deadline)) {
                            LOGGER.info("awaitUntil(deadline) 最后期限已到：= {}", deadline);
                            return;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 条件匹配执行操作
                i++;
                LOGGER.info("i = {}", i);
            } finally {
                CONDITION.signalAll();
                REENTRANT_LOCK.unlock();
            }
        }).start();
    }

}

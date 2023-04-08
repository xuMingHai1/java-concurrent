package xyz.xuminghai.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 2023/4/6 14:45 星期四<br/>
 *
 * <h1>ReentrantReadWriteLock示例1</h1>
 * ReentrantReadWriteLocks 可用于在某些类型的集合的某些使用中提高并发性。
 * 这通常只有在预期集合很大、由比写入线程更多的读取线程访问并且需要的开销超过同步开销的操作时才值得。
 * <p>锁定降级</p>
 * 重入还允许从写锁降级为读锁，方法是获取写锁，然后获取读锁，然后释放写锁。
 * 但是，从读锁升级到写锁是不可能的。
 * <p>Condition支持</p>
 * 写锁提供了一个Condition实现，对于写锁，其行为方式与ReentrantLock.newCondition为ReentrantLock提供的Condition实现相同。
 * 当然，这个Condition只能与写锁一起使用。<br/>
 * 读锁不支持Condition并且readLock().newCondition()抛出UnsupportedOperationException 。
 *
 * @author xuMingHai
 */
public class ReentrantReadWriteLockDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReentrantReadWriteLockDemo01.class);

    /**
     * 读写锁，内部分别创建了读写锁，默认是非公平策略
     */
    private static final ReentrantReadWriteLock REENTRANT_READ_WRITE_LOCK = new ReentrantReadWriteLock();

    /**
     * 读锁
     */
    private static final ReentrantReadWriteLock.ReadLock READ_LOCK = REENTRANT_READ_WRITE_LOCK.readLock();

    /**
     * 写锁
     */
    private static final ReentrantReadWriteLock.WriteLock WRITE_LOCK = REENTRANT_READ_WRITE_LOCK.writeLock();

    /**
     * 共享资源，基于happen-before的可见性
     */
    private static int i;


    public static void main(String[] args) {

        // 读操作
        new Thread(ReentrantReadWriteLockDemo01::loopSleepPrint, "t1").start();

        // 读操作
        new Thread(ReentrantReadWriteLockDemo01::loopSleepPrint, "t2").start();

        new Thread(() -> {
            // 写操作
            for (; ; ) {
                // 休眠 1 秒
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 使用写锁设置随机值
                WRITE_LOCK.lock();
                try {
                    // 设置 i 为随机 0 ~ 9
                    i = (int) (Math.random() * 10);
                    LOGGER.info("设置 i = {}", i);
                    // 如果条件符合，不在休眠跳出循环
                    if (i == 9) {
                        break;
                    }
                } finally {
                    WRITE_LOCK.unlock();
                }
            }
        }, "t3").start();

        // 读操作
        loopSleepPrint();
    }

    private static void loopSleepPrint() {
        for (; ; ) {
            // 休眠 500 毫秒
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 使用读锁获取最新值
            READ_LOCK.lock();
            try {
                LOGGER.info("i = {}", i);
                if (i == 9) {
                    break;
                }
            } finally {
                READ_LOCK.unlock();
            }
        }
    }

}

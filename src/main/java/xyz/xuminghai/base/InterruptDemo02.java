package xyz.xuminghai.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 2022/3/2 5:31 星期三<br/>
 * <h1>中断示例</h1>
 * 中断线程是对当线程处于WAITING或TIMED_WAITING状态时唤醒线程。具体的线程状态{@link java.lang.Thread.State}
 *
 * @author xuMingHai
 */
public class InterruptDemo02 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InterruptDemo02.class);

    /*
        想要中断一个线程就需要线程处于WAITING或TIMED_WAITING状态。
        什么时候线程处于这种状态呢？
        1.使用Object.wait()、Thread.join()、LockSupport.park()这些方法会让线程处于WAITING状态。
        2.使用Object.wait(long)、Thread.sleep(long)、Thread.join(long)、LockSupport.parkNanos(long)
        这些方法会让线程处于TIMED_WAITING状态。
     */

    public static void main(String[] args) {

        // 创建t1线程，让其处于WAITING状态
        final Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                LockSupport.park();
                // 判断线程是否被中断，请注意
                /*
                    请注意
                    isInterrupted() 获取线程是否被中断，不会改变中断状态
                    Thread.interrupted() 获取线程是否被中断，然后改变线程为非中断状态
                 */
                if (isInterrupted()) {
                    LOGGER.info("t1线程被中断");
                }
            }
        };

        // 创建t2线程，让其处于TIMED_WAITING状态

        final Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                // 等待10秒
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
                // 判断线程是否被中断
                if (isInterrupted()) {
                    LOGGER.info("t2线程被中断");
                }

            }
        };

        // 启动线程
        t1.start();
        t2.start();

        // 休眠1秒等待线程运行，打印线程状态
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOGGER.info("t1线程的状态：{}", t1.getState());
            LOGGER.info("t2线程的状态：{}", t2.getState());
        }


        // 中断线程，改变线程状态
        t1.interrupt();
        t2.interrupt();

    }

}

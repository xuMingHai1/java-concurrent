package xyz.xuminghai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 2023/5/3 20:29 星期三<br/>
 * <h1>CyclicBarrier示例02</h1>
 * CyclicBarrier 的Runnable命令执行的线程，由最后一个到达屏障的线程执行Runnable命令
 *
 * @author xuMingHai
 */
public class CyclicBarrierDemo02 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicBarrierDemo02.class);

    private static final CyclicBarrier CYCLIC_BARRIER = new CyclicBarrier(3,
            () -> LOGGER.info("Runnable"));

    public static void main(String[] args) {

        new Thread(() -> {
            LOGGER.info("Start");
            try {
                int await = CYCLIC_BARRIER.await();
                LOGGER.info("await = {}", await);
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("End");
        }, "t1").start();

        new Thread(() -> {
            LOGGER.info("Start");
            try {
                int await = CYCLIC_BARRIER.await();
                LOGGER.info("await = {}", await);
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("End");
        }, "t2").start();

        LOGGER.info("Start");
        try {
            int await = CYCLIC_BARRIER.await();
            LOGGER.info("await = {}", await);
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("End");

    }

}

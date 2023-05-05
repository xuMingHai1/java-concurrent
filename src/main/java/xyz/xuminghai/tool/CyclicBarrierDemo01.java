package xyz.xuminghai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 2023/5/2 2:45 星期二<br/>
 *
 * <h1>CyclicBarrier示例01</h1>
 * 允许一组线程全部等待彼此到达公共屏障点的同步辅助工具。
 * 之所以称为循环屏障，是因为它可以在等待线程被释放后重新使用<br/>
 * CyclicBarrier支持可选的Runnable命令，该命令在每个障碍点运行一次，在最后一个
 * 线程到达之后，但在释放任何线程之前运行。这在对共享状态更新很有用
 *
 * @author xuMingHai
 */
public class CyclicBarrierDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicBarrierDemo01.class);

    private static final CyclicBarrier CYCLIC_BARRIER = new CyclicBarrier(3);


    public static void main(String[] args) {
        worker(2);
        try {
            CYCLIC_BARRIER.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("重置屏障");
        // 屏障释放后，重置屏障
        CYCLIC_BARRIER.reset();
        worker(3);
    }

    private static void worker(int threadNumber) {
        for (int j = 0; j < threadNumber; j++) {
            new Thread(() -> {
                LOGGER.info("Start");
                try {
                    CYCLIC_BARRIER.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                LOGGER.info("End");
            }).start();
        }
    }


}

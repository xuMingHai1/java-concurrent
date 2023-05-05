package xyz.xuminghai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;

/**
 * 2023/5/4 5:34 星期四<br/>
 * <h1>Phaser示例01</h1>
 * 一个可重用的同步屏障，类似于CountDownLatch和CyclicBarriar。
 * 通过向Phaser注册（无法确定线程已经注册）和注销，和到达和等待其他阶段进行控制
 *
 * @author xuMingHai
 */
public class PhaserDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PhaserDemo01.class);

    /**
     * 代理CountDownLatch，一个以注册，未到达
     */
    private static final Phaser PHASER = new Phaser(1);

    public static void main(String[] args) {
        // 启动任务
        runTasks();
        LOGGER.info("Tasks ready");
        // 获取以注册的数量
        LOGGER.info("PHASER.getRegisteredParties() = {}", PHASER.getRegisteredParties());
        // 获取以到达的数量
        LOGGER.info("PHASER.getArrivedParties() = {}", PHASER.getArrivedParties());
        // 获取未到达的数量
        LOGGER.info("PHASER.getUnarrivedParties() = {}", PHASER.getUnarrivedParties());
        // 到达并注销
        PHASER.arriveAndDeregister();
    }

    private static void runTasks() {
        for (int i = 0; i < 3; i++) {
            // 此处注册确保创建很多线程时，注册的准确性
            PHASER.register();
            new Thread(() -> {
                // 到达等待
                PHASER.arriveAndAwaitAdvance();
                LOGGER.info("Ok");
            }).start();
        }
    }

}

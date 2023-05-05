package xyz.xuminghai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 2023/5/5 15:27 星期五<br/>
 * <h1>Phaser示例02</h1>
 * 分层。移相器可以分层（即，以树结构构建）以减少争用。<br/>
 * 在分层移相器树中，自动管理子移相器与其父移相器的注册和注销。
 * 每当子移相器的注册方数量变为非零时（如在Phaser(Phaser, int)构造函数、
 * register或bulkRegister中建立的那样），子移相器就会向其父移相器注册。
 * 每当注册方的数量因调用arriveAndDeregister而变为零时，子移相器就会从其父移相器中注销
 *
 * @author xuMingHai
 */
public class PhaserDemo02 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PhaserDemo02.class);

    private static final Phaser ROOT_PHASER = new Phaser(1);


    public static void main(String[] args) {
        task1();
        task2();
        LOGGER.info("ROOT_PHASER.getRegisteredParties() = {}", ROOT_PHASER.getRegisteredParties());
        // 等待子任务完成
        ROOT_PHASER.arriveAndAwaitAdvance();
        LOGGER.info("ROOT_PHASER.getRegisteredParties() = {}", ROOT_PHASER.getRegisteredParties());
        LOGGER.info("Child task Ok");
    }

    private static void task1() {
        Phaser phaser = new Phaser(ROOT_PHASER, 3);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep((long) (Math.random() * 5));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                LOGGER.info("Ok");
                // 到达并注销
                phaser.arriveAndDeregister();
            }).start();
        }
    }

    private static void task2() {
        Phaser phaser = new Phaser(ROOT_PHASER, 1);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep((long) (Math.random() * 5));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("Ok");
            // 到达并注销
            phaser.arriveAndDeregister();
        }).start();
    }

}

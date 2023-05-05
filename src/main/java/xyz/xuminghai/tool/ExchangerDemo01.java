package xyz.xuminghai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * 2023/5/5 11:41 星期五<br/>
 * <h1>Exchanger示例01</h1>
 * 两个线程交换数据的同步点
 *
 * @author xuMingHai
 */
public class ExchangerDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangerDemo01.class);

    /**
     * 交换器可能在遗传算法和管道设计等应用中很有用。
     */
    private static final Exchanger<String> EXCHANGER = new Exchanger<>();

    public static void main(String[] args) {

        new Thread(() -> {
            String s = "123";
            LOGGER.info("s = {}", s);
            try {
                // 休眠1秒
                TimeUnit.SECONDS.sleep(1);
                // 使用同步交换器交换数据
                s = EXCHANGER.exchange(s);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("s = {}", s);
        }).start();

        String s = "456";
        LOGGER.info("s = {}", s);
        try {
            // 使用同步交换器交换数据
            s = EXCHANGER.exchange(s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("s = {}", s);

    }
}

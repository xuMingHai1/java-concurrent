package xyz.xuminghai.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 2023/3/15 21:19 星期三<br/>
 *
 * <h1>线程栈信息</h1>
 *
 * @author xuMingHai
 */
public class ThreadStackDemo {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadStackDemo.class);


    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            List<String> list = new LinkedList<>();
            list.add("");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            list.add("1");
            LOGGER.info("list = {}", list);
            list.clear();
        }, "t1");

        t1.start();

        // 直到线程死亡停止循环
        while (t1.isAlive()) {
            LOGGER.info(Arrays.toString(t1.getStackTrace()));
        }
    }

}

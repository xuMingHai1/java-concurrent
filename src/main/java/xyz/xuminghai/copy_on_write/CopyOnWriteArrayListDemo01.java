package xyz.xuminghai.copy_on_write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 2023/5/16 22:30 星期二<br/>
 * <h1>CopyOnWriteArrayList示例01</h1>
 * ArrayList的线程安全变体，其中所有可变操作（ add 、 set等）都是通过制作底层数组的新副本来实现的。
 *
 * @author xuMingHai
 */
public class CopyOnWriteArrayListDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyOnWriteArrayListDemo01.class);

    /**
     * 仅在写入时加锁对副本进行修改，最后将修改后的副本替换为原始数组，最终一致性
     * 适用于对并发时数据的精确度不高，读多写少时使用
     */
    private static final CopyOnWriteArrayList<String> COPY_ON_WRITE_ARRAY_LIST =
            new CopyOnWriteArrayList<>(new String[]{"0", "1", "2", "3", "4"});


    public static void main(String[] args) {

        // 多线程并发访问修改
        new Thread(() -> {
            LOGGER.info("list = {}", COPY_ON_WRITE_ARRAY_LIST);
            COPY_ON_WRITE_ARRAY_LIST.set(0, Thread.currentThread().getName());
            LOGGER.info("list = {}", COPY_ON_WRITE_ARRAY_LIST);
        }).start();

        new Thread(() -> {
            LOGGER.info("list = {}", COPY_ON_WRITE_ARRAY_LIST);
            COPY_ON_WRITE_ARRAY_LIST.set(COPY_ON_WRITE_ARRAY_LIST.size() - 1,
                    Thread.currentThread().getName());
            LOGGER.info("list = {}", COPY_ON_WRITE_ARRAY_LIST);
        }).start();

        // 遍历的只是快照，而不是最新值
        for (String s : COPY_ON_WRITE_ARRAY_LIST) {
            LOGGER.info("s = {}", s);
        }

    }

}

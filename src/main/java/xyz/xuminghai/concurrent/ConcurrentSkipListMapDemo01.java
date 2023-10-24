package xyz.xuminghai.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 2023/10/1 21:44 星期日<br/>
 * 可扩展的并发ConcurrentNavigableMap实现。映射根据其键的自然顺序进行排序，
 * 或者通过映射创建时提供的Comparator进行排序，具体取决于使用的构造函数。
 * 此类实现了SkipLists 的并发变体，为containsKey 、 get 、
 * put和remove操作及其变体提供预期的平均log(n)时间成本。插入、删除、更新和访问操作由多个线程安全地并发执行。
 * 映射根据其键的自然顺序进行排序
 *
 * @author xuMingHai
 */
public class ConcurrentSkipListMapDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentSkipListMapDemo01.class);

    /**
     * 根据键的自然顺序进行排序。
     */
    private static final ConcurrentSkipListMap<Integer, String> CONCURRENT_SKIP_LIST_MAP = new ConcurrentSkipListMap<>();

    public static void main(String[] args) {
        CONCURRENT_SKIP_LIST_MAP.put(1, "张三");
        CONCURRENT_SKIP_LIST_MAP.put(3, "王五");
        CONCURRENT_SKIP_LIST_MAP.put(4, "赵六");

        // 并行写入
        new Thread(() -> {
            CONCURRENT_SKIP_LIST_MAP.put(2, "李四");
            CONCURRENT_SKIP_LIST_MAP.entrySet().forEach(entry -> LOGGER.info(entry.toString()));
        }).start();

        // 弱一致性遍历
        CONCURRENT_SKIP_LIST_MAP.entrySet().forEach(entry -> LOGGER.info(entry.toString()));
    }

}

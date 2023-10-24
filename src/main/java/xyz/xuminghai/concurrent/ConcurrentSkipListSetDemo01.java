package xyz.xuminghai.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 2023/10/1 22:03 星期日<br/>
 * 基于ConcurrentSkipListMap的可扩展并发NavigableSet实现。集合的元素根据其自然顺序或通过在集合创建时提供的Comparator进行排序，具体取决于使用的构造函数。
 * 此实现为contains 、 add和remove操作及其变体提供了预期的平均log(n)时间成本。插入、删除和访问操作由多个线程同时安全地执行。
 * 迭代器和分裂器是弱一致的。
 *
 * @author xuMingHai
 */
public class ConcurrentSkipListSetDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentSkipListSetDemo01.class);

    /**
     * 根据元素的自然顺序进行排序。
     */
    private static final ConcurrentSkipListSet<Integer> CONCURRENT_SKIP_LIST_SET = new ConcurrentSkipListSet<>();

    public static void main(String[] args) {
        CONCURRENT_SKIP_LIST_SET.add(1);
        CONCURRENT_SKIP_LIST_SET.add(3);
        CONCURRENT_SKIP_LIST_SET.add(4);

        // 并行写入
        new Thread(() -> {
            CONCURRENT_SKIP_LIST_SET.add(2);
            CONCURRENT_SKIP_LIST_SET.forEach(item -> LOGGER.info(item.toString()));
        }).start();

        // 弱一致性遍历
        CONCURRENT_SKIP_LIST_SET.forEach(item -> LOGGER.info(item.toString()));
    }


}

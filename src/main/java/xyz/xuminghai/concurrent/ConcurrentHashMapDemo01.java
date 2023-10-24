package xyz.xuminghai.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 2023/8/2 10:34 星期三<br/>
 * 支持完全并发检索和高预期更新并发的哈希表。此类遵循与Hashtable相同的功能规范，
 * 并包含与Hashtable的每个方法相对应的方法版本。然而，即使所有操作都是线程安全的，
 * 检索操作也不需要锁定，并且不支持以阻止所有访问的方式锁定整个表。在依赖其线程安全性但不依赖其同步细节的程序中，
 * 此类可与Hashtable完全互操作。
 *
 * @author xuMingHai
 */
public class ConcurrentHashMapDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentHashMapDemo01.class);

    private static final ConcurrentHashMap<String, String> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        // 填充初始数据
        CONCURRENT_HASH_MAP.put("key1", "value1");
        CONCURRENT_HASH_MAP.put("key2", "value2");
        CONCURRENT_HASH_MAP.put("key3", "value3");

        new Thread(() -> {
            // 多线程修改同一个key
            CONCURRENT_HASH_MAP.replace("key1", "thread0");
            // parallelismThreshold – 并行执行此操作所需的（估计）元素数量
            // 串行执行
            CONCURRENT_HASH_MAP.forEachEntry(Long.MAX_VALUE, entry -> LOGGER.info(entry.toString()));
        }).start();

        new Thread(() -> {
            // 多线程修改同一个key
            CONCURRENT_HASH_MAP.replace("key1", "thread1");
            // 并行执行
            CONCURRENT_HASH_MAP.forEachEntry(Long.MIN_VALUE, entry -> LOGGER.info(entry.toString()));
        }).start();
    }

}

package xyz.xuminghai.copy_on_write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 2023/5/16 22:51 星期二<br/>
 * <h1>CopyOnWriteArraySet示例01</h1>
 * 一个使用内部CopyOnWriteArrayList进行所有操作的Set 。因此，它具有相同的基本属性：
 * 它最适合集合大小通常保持较小、只读操作远远多于可变操作的应用程序，并且您需要防止遍历期间线程之间的干扰。
 * 它是线程安全的。
 * 可变操作（ add 、 set 、 remove等）很昂贵，因为它们通常需要复制整个底层数组。
 * 迭代器不支持可变remove操作。
 * 通过迭代器遍历速度很快，不会遇到其他线程的干扰。迭代器依赖于在构造迭代器时数组不变的快照。
 *
 * @author xuMingHai
 */
public class CopyOnWriteArraySetDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyOnWriteArraySetDemo01.class);

    /**
     * 实现Set接口的CopyOnWriteArrayList
     */
    private static final CopyOnWriteArraySet<String> COPY_ON_WRITE_ARRAY_SET = new CopyOnWriteArraySet<>();

    public static void main(String[] args) {
        COPY_ON_WRITE_ARRAY_SET.add("1");
        COPY_ON_WRITE_ARRAY_SET.add("2");
        COPY_ON_WRITE_ARRAY_SET.add("3");
        for (int i = 0; i < 2; i++) {
            COPY_ON_WRITE_ARRAY_SET.add("4");
        }
        LOGGER.info("set = {}", COPY_ON_WRITE_ARRAY_SET);
    }

}

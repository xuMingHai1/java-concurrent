package xyz.xuminghai.atomic;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.xuminghai.base.SynchronizedDemo03;

import static xyz.xuminghai.base.SynchronizedDemo03.FREQUENCY;

/**
 * 2022/3/6 21:09 星期日<br/>
 * <h1>原子整型和长整型示例</h1>
 *
 * @author xuMingHai
 */
public class AtomicIntegerAndAtomicLongDemo01 {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AtomicIntegerAndAtomicLongDemo01.class);

	/**
	 * 原子整型
	 */
	private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();


	public static void main(String[] args) {
		// 使用synchronize示例做比较
		System.out.println("------------------Synchronize示例------------------");
		new SynchronizedDemo03(true).run();

		System.out.println("------------------AtomicInteger示例------------------");
		run();

	}

	public static void run() {
		// t1线程自增
		final Thread t1 = new Thread(() -> {
			for (int i = 0; i < FREQUENCY; i++) {
				ATOMIC_INTEGER.incrementAndGet();
			}
		}, "t1");

		// t2线程自减
		final Thread t2 = new Thread(() -> {
			for (int i = 0; i < FREQUENCY; i++) {
				ATOMIC_INTEGER.decrementAndGet();
			}
		}, "t2");

		// 启动线程
		t1.start();
		t2.start();

		// 记录开始时间
		final long startTimeMillis = System.currentTimeMillis();

		// 等待两个线程运行结束
		try {
			t1.join();
			t2.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			// 获取最后的结果，请区分加锁和不加锁的区别
			LOGGER.info("ATOMIC_INTEGER = {}", ATOMIC_INTEGER.get());
			LOGGER.info("运行时长：{}（毫秒）", System.currentTimeMillis() - startTimeMillis);
		}

	}

}

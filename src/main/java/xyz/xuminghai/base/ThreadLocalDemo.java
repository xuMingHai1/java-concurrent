package xyz.xuminghai.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2023/3/15 23:34 星期三<br/>
 *
 * <h1>线程局部变量示例</h1>
 * 每个线程都存在这个变量的副本，每个线程都只使用自己的ThreadLocalMap维护变量，key为ThreadLocal，value为变量
 *
 * @author xuMingHai
 */
public class ThreadLocalDemo {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalDemo.class);

	/**
	 * 使用类变量，可以避免不必要的弱引用清除key
	 */
	private static final ThreadLocal<Integer> THREAD_LOCAL = ThreadLocal.withInitial(() -> 0);


	public static void main(String[] args) {

		new Thread(() -> {
			LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());
			THREAD_LOCAL.set(1);
			LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());
		}, "t1").start();

		new Thread(() -> {
			LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());
			THREAD_LOCAL.set(2);
			LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());
		}, "t2").start();

		new Thread(() -> {
			LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());
			THREAD_LOCAL.set(3);
			LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());
		}, "t3").start();

		LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());
	}

}

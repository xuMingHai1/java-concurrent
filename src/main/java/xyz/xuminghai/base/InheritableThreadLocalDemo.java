package xyz.xuminghai.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2023/3/15 23:51 星期三<br/>
 *
 * <h1>可继承的本地变量</h1>
 * 适用于父线程创建子线程（由父线程创建的线程）时传递线程局部变量
 * <p color = "red">注意子线程只是创建时局部变量来自父线程，也是完全独立的副本，不会跟随父线程的局部变量变化而变化</p>
 *
 * @author xuMingHai
 */
public class InheritableThreadLocalDemo {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InheritableThreadLocalDemo.class);

	private static final ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return "张三";
		}
	};


	public static void main(String[] args) throws InterruptedException {

		Thread t1 = new Thread(() -> LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get()),
				"t1");
		t1.start();
		t1.join();

		LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());

		THREAD_LOCAL.set("李四");
		Thread t2 = new Thread(() -> LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get()),
				"t2");
		t2.start();
		LOGGER.info("THREAD_LOCAL = {}", THREAD_LOCAL.get());

	}

}

package xyz.xuminghai.base;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2023/3/15 21:45 星期三<br/>
 *
 * <h1>线程未捕获异常示例</h1>
 *
 * @author xuMingHai
 */
public class ThreadUncaughtExceptionDemo {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUncaughtExceptionDemo.class);


	public static void main(String[] args) {

		Thread t1 = new Thread(() -> {
			throw new IllegalStateException();
		}, "t1");

		// 设置线程因为未捕获的异常导致线程终止处理器
		t1.setUncaughtExceptionHandler((t, e) -> LOGGER.error("出现异常了"));

		t1.start();

		try {
			TimeUnit.SECONDS.sleep(3);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

}

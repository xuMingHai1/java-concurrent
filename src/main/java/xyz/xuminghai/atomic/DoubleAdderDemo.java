package xyz.xuminghai.atomic;

import java.util.concurrent.atomic.DoubleAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2023/3/22 20:03 星期三<br/>
 *
 * <h1>DoubleAdder示例</h1>
 * 与LongAdder类似，将double转为原始的64位long类型
 *
 * @author xuMingHai
 */
public class DoubleAdderDemo {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DoubleAdderDemo.class);


	public static void main(String[] args) {
		// double类型累加器
		final DoubleAdder doubleAdder = new DoubleAdder();

		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				doubleAdder.add(0.5);
			}
		}, "t1");

		Thread t2 = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				doubleAdder.add(0.05);
			}
		}, "t2");

		Thread t3 = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				doubleAdder.add(0.005);
			}
		}, "t3");

		t1.start();
		t2.start();
		t3.start();

		try {
			t1.join();
			t2.join();
			t3.join();
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		LOGGER.info("doubleAdder = {}", doubleAdder);
	}

}

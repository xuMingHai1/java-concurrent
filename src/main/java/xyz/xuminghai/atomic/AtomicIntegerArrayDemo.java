package xyz.xuminghai.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 2023/3/13 17:30 星期一<br/>
 *
 * <h1>AtomicIntegerArray示例</h1>
 * 可以看做是一组AtomicInteger
 *
 * @author xuMingHai
 */
public class AtomicIntegerArrayDemo {

	private static final AtomicIntegerArray ATOMIC_INTEGER_ARRAY = new AtomicIntegerArray(3);

	public static void main(String[] args) {
		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 300; i++) {
				if ((i % 2) == 0) {
					ATOMIC_INTEGER_ARRAY.getAndIncrement(1);
				}
				else if ((i % 3) == 0) {
					ATOMIC_INTEGER_ARRAY.getAndIncrement(2);
				}
				else {
					ATOMIC_INTEGER_ARRAY.getAndIncrement(0);
				}
			}

		}, "t1");

		Thread t2 = new Thread(() -> {
			for (int i = 0; i < 200; i++) {
				if ((i % 2) == 0) {
					ATOMIC_INTEGER_ARRAY.getAndDecrement(1);
				}
				else if ((i % 3) == 0) {
					ATOMIC_INTEGER_ARRAY.getAndDecrement(2);
				}
				else {
					ATOMIC_INTEGER_ARRAY.getAndDecrement(0);
				}
			}

		}, "t2");

		Thread t3 = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				if ((i % 2) == 0) {
					ATOMIC_INTEGER_ARRAY.getAndDecrement(1);
				}
				else if ((i % 3) == 0) {
					ATOMIC_INTEGER_ARRAY.getAndDecrement(2);
				}
				else {
					ATOMIC_INTEGER_ARRAY.getAndDecrement(0);
				}
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

		System.out.println(ATOMIC_INTEGER_ARRAY);
	}

}

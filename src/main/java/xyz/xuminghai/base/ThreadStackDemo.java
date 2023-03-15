package xyz.xuminghai.base;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 2023/3/15 21:19 星期三<br/>
 *
 * <h1>线程栈信息</h1>
 *
 * @author xuMingHai
 */
public class ThreadStackDemo {

	public static void main(String[] args) {
		Thread t1 = new Thread(() -> {
			List<String> list = new LinkedList<>();
			list.add("");
			try {
				TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			list.add("1");
			System.out.println("list = " + list);
			list.clear();
		}, "t1");

		t1.start();

		// 直到线程死亡停止循环
		while (t1.isAlive()) {
			System.out.println(Arrays.toString(t1.getStackTrace()));
		}
	}

}

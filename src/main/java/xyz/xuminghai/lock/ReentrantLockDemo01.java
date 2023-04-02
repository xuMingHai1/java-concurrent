package xyz.xuminghai.lock;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2023/3/29 3:56 星期三<br/>
 *
 * <h1>ReentrantLock示例01</h1>
 * ReentrantLock的lock、unlock使用
 *
 * @author xuMingHai
 */
public class ReentrantLockDemo01 {

	/**
	 * 可重入锁，已获取到锁的线程可以重复获取锁，默认是非公平锁
	 */
	private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReentrantLockDemo01.class);

	private static final boolean IS_LOCK = true;

	/**
	 * 多线程共享资源
	 */
	private static int i;

	public static void main(String[] args) {
		// 保存线程引用
		Thread[] threads = new Thread[3];
		// 创建线程
		for (int j = 0; j < 3; j++) {
			// 多线程分别自增
			Thread thread = new Thread(() -> {
				for (int k = 0; k < 1_000_000; k++) {
					if (IS_LOCK) {
						lockIncrement();
					}
					else {
						NonLockIncrement();
					}
				}
			});
			threads[j] = thread;
			thread.start();
		}

		// 等待所有线程执行完毕
		for (Thread thread : threads) {
			try {
				thread.join();
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		LOGGER.info("i = {}", i);
	}

	private static void lockIncrement() {
		// 规范写法
		REENTRANT_LOCK.lock();
		try {
			// 基于 happen-before，unlock之前的操作对lock之后可见，确保此时 i 是最新值
			i++;
		}
		finally {
			// 防止出现异常而没有释放锁
			REENTRANT_LOCK.unlock();
		}
	}

	private static void NonLockIncrement() {
		i++;
	}

}

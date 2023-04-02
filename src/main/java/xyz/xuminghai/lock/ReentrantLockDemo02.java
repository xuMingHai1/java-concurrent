package xyz.xuminghai.lock;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2023/3/29 5:12 星期三<br/>
 *
 * <h1>ReentrantLock示例02</h1>
 * ReentrantLock的lockInterruptibly、tryLock、tryLock(timeout, unit)使用
 *
 * @author xuMingHai
 */
public class ReentrantLockDemo02 {

	/**
	 * 可重入锁，已获取到锁的线程可以重复获取锁，默认是非公平锁
	 */
	private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReentrantLockDemo02.class);

	private static final Task[] TASKS = new Task[] {new Task(), new Task(), new Task()};

	public static void main(String[] args) {
		final Task t1 = TASKS[0], t2 = TASKS[1], t3 = TASKS[2];

		// t1 lockInterruptibly
		t1.setTask(() -> {
			// 除非当前线程被中断，否则获取锁
			try {
				REENTRANT_LOCK.lockInterruptibly();
				try {
					LOGGER.info("获取到锁");
					// 如果t1获取到锁，中断t3
					t3.interrupt();
					// 没有其他线程执行时释放锁
					liveLoop(t1);
				}
				finally {
					REENTRANT_LOCK.unlock();
				}
			}
			catch (InterruptedException e) {
				// 已经有其他线程获取到锁，线程被中断
				LOGGER.info("线程被中断，获取锁失败");
			}
		});

		// t2 tryLock
		t2.setTask(() -> {
			// tryLock 立即返回是否获取到锁，公平锁时也会立即返回，可以使用tryLock(0, timeUnit)使用公平获取锁
			if (REENTRANT_LOCK.tryLock()) {
				try {
					LOGGER.info("获取到锁");
					// 如果t2获取到锁，中断t1、t3
					t1.interrupt();
					t3.interrupt();
					// 没有其他线程执行时释放锁
					liveLoop(t2);
				}
				finally {
					REENTRANT_LOCK.unlock();
				}
				return;
			}
			// 没有获取到锁
			LOGGER.info("获取锁失败");
		});

		// t3 tryLock(timeout, unit)
		t3.setTask(() -> {
			try {
				if (REENTRANT_LOCK.tryLock(0, TimeUnit.SECONDS)) {
					try {
						LOGGER.info("获取到锁");
						// 如果t3获取到锁，中断t2
						t2.interrupt();
						// 没有其他线程执行时释放锁
						liveLoop(t3);
					}
					finally {
						REENTRANT_LOCK.unlock();
					}
				}
				// 等待时间到了
				LOGGER.info("等待时间到了，获取锁失败");
			}
			catch (InterruptedException e) {
				// 已经有其他线程获取到锁，线程被中断
				LOGGER.info("线程被中断，获取锁失败");
			}
		});

		// 启动线程
		t1.start();
		t2.start();
		t3.start();
	}

	private static void liveLoop(Task t) {
		// 自旋等待其他线程执行完成
		for (Task task : TASKS) {
			LOGGER.info(task.toString());
			if (task != t && task.getLive() >= 0) {
				liveLoop(t);
			}
		}
	}

	private static class Task extends Thread {

		private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

		private Runnable task;

		/**
		 * 存活属性，0 初始化，1 执行中， -1 执行结束
		 */
		private volatile int live;

		public Task() {
			super("task-" + ATOMIC_INTEGER.getAndIncrement());
		}

		@Override
		public void run() {
			live = 1;
			if (task != null) {
				task.run();
			}
			live = -1;
			ATOMIC_INTEGER.getAndDecrement();
		}

		public void setTask(Runnable task) {
			this.task = task;
		}

		public int getLive() {
			return live;
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", super.getName() + " (", ")")
					.add("live = " + live)
					.toString();
		}
	}

}

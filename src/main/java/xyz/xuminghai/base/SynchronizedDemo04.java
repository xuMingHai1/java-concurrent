package xyz.xuminghai.base;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2022/3/2 21:01 星期三<br/>
 * <h1>wait()、notify()、notifyAll()示例</h1>
 * wait()、notify()、notifyAll()是Object类的方法，使用这些方法需要这个对象先获得synchronize对象锁，
 * 锁对象去使用这些方法。<br/>
 * 比方说，当一个线程需要的条件还不符合时，就可以先使用wait()进行等待，在条件符合时，别的线程使用notify()去唤醒这个线程
 *
 * @author xuMingHai
 */
public class SynchronizedDemo04 {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedDemo04.class);

	/**
	 * 锁对象
	 */
	private static final Object LOCK = new Object();


    /*
        猜想有下面情景：
        小红和小蓝两个线程，他们是合作干活的，小红需要等小蓝完成工作后才能接着进行工作
     */

	/**
	 * 模拟小蓝是否工作遇到了问题，是为true
	 */
	private static final boolean T2_INTERRUPTED = false;

	/**
	 * 小蓝的工作时长（毫秒），小红的工作时长是小蓝的一半，但是需要等小蓝工作完，才能接着工作
	 */
	private static final long T2_SLEEP = 1_000;

	public static void main(String[] args) {

		// 小红线程
		final Thread t1 = new Thread("小红") {
			@Override
			public void run() {
				LOGGER.info("小红开始工作了");
				// 小红的工作时长只有小蓝的一半
				try {
					TimeUnit.MILLISECONDS.sleep(T2_SLEEP / 2);
				}
				catch (InterruptedException e) {
					LOGGER.error("收到小蓝的通知工作出现了问题", e);
					// 设置中断状态，捕获中断异常，会重置线程的中断状态
					interrupt();
				}

				if (isInterrupted()) {
					LOGGER.error("小红任务失败了");
				}
				else {
					// 同步块
					synchronized (LOCK) {
						// 进入等待状态，等待小蓝完成通知
						try {
							LOCK.wait();
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						finally {

							LOGGER.info("小红完成任务了");
						}

					}
				}

			}
		};

		// 小蓝线程
		final Thread t2 = new Thread("小蓝") {
			@Override
			public void run() {
				LOGGER.info("小蓝开始工作了");
				// 休眠1秒模拟小蓝工作
				try {
					TimeUnit.MILLISECONDS.sleep(T2_SLEEP);
				}
				catch (InterruptedException e) {
					LOGGER.error("工作出现问题了，通知小红不要工作了", e);
					// 中断小红的工作
					t1.interrupt();
					interrupt();
				}


				// 工作是否顺利完成
				if (isInterrupted()) {
					LOGGER.error("小蓝任务失败了");
				}
				else {
					synchronized (LOCK) {
						LOGGER.info("小蓝顺利完成任务了，开始通知小红");
						// 通知小红不用等待了，可以工作了
						LOCK.notify();
					}
				}

			}
		};

		// 启动线程
		t1.start();
		t2.start();

		// 是否小蓝工作出现问题
		if (T2_INTERRUPTED) {
			t2.interrupt();
		}

	}

}

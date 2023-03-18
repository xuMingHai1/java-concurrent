package xyz.xuminghai.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2022/3/2 5:13 星期三<br/>
 * <h1>启动线程和正确关闭线程</h1>
 * @author xuMingHai
 */
public class StartThreadDemo01 {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StartThreadDemo01.class);

    /*
        Thread类中有一个Runnable接口的属性，这是创建线程的本质。
        而这个接口的run方法就是我们使用线程的根本。
     */

	public static void main(String[] args) {
		// 创建一个线程并运行，默认创建的是非守护线程。
		new Thread(() -> LOGGER.info("新线程开始运行")).start();

        /*
            正确关闭线程的方式是让线程内的任务运行完毕。
            虽然Thread类有stop方法和destroy方法，但已经被弃用，不建议使用。
         */

		// 使用主线程打印语句
		LOGGER.info("主线程运行");
	}

}

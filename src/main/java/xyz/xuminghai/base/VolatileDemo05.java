package xyz.xuminghai.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2022/3/4 5:14 星期五<br/>
 * <h1>volatile示例</h1>
 * volatile关键字的作用：
 * <ul>
 *     <li>内存可见性，基于内存可见性，就可以实现一个线程写，多个线程读的原子性</li>
 *     <li>防止指令重排序</li>
 * </ul>
 * 更加详细的测试请参见 concurrency-test模块的VolatileTest
 *	<p color="red">happen-before 是熟练掌握和理解并发编程的重点</p>
 * <hr/>
 * <h3>内存一致性属性（happen-before）</h3>
 * <a href="https://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html#jls-17.4.5">Java Language Specification 的第 17 章</a> 定义了
 * 内存操作（例如共享变量的读取和写入）的happens-before关系
 * 仅当写入操作发生在读取操作之前时，一个线程的写入结果才能保证对另一个线程的读取可见。
 * synchronized和volatile构造以及Thread.start()和Thread.join()方法可以形成happens-before关系
 * <p>尤其：</p>
 * <ul>
 *     <li>
 *         线程中的每个动作都发生在该线程中按程序顺序稍后出现的每个动作之前。
 *     </li>
 *     <li>
 *         监视器的解锁（ synchronized块或方法退出）发生在同一监视器的每个后续锁定（ synchronized块或方法入口）之前。
 *         并且因为happens-before关系是可传递的，一个线程在解锁之前的所有操作都发生在任何线程锁定该监视器之后的所有操作之前。
 *     </li>
 *     <li>
 *         对volatile字段的写入发生在同一字段的每次后续读取之前。 volatile字段的写入和读取具有与进入和退出监视器类似的内存一致性效果，
 *         但不需要互斥锁定。
 *     </li>
 *     <li>
 *         在线程上start的调用发生在启动线程中的任何操作之前。
 *     </li>
 *     <li>
 *         线程中的所有操作发生在任何其他线程从该线程上的join成功返回之前。
 *     </li>
 * </ul>
 * java.util.concurrent及其子包中所有类的方法将这些保证扩展到更高级别的同步。
 * <p>尤其：</p>
 * <ul>
 *     <li>
 *         在将对象放入任何并发集合之前线程中的操作发生在另一个线程中从集合中访问或删除该元素之后的操作之前。
 *     </li>
 *     <li>
 *         在将Runnable提交给Executor之前线程中的操作发生在其执行开始之前。同样对于提交给ExecutorService Callables 。
 *     </li>
 *     <li>
 *         在另一个线程中通过Future.get()检索结果之后，由Future表示的异步计算采取的操作发生在操作之前。
 *     </li>
 *     <li>
 *         “释放”同步器方法（例如Lock.unlock 、 Semaphore.release和CountDownLatch.countDown之前的操作
 *         发生在成功“获取”方法（例如Lock.lock 、 Semaphore.acquire 、 Condition.await和CountDownLatch.await ）之后的操作之前CountDownLatch.await在另一个线程中的同一个同步器对象上。
 *     </li>
 *     <li>
 *         对于通过Exchanger成功交换对象的每对线程，每个线程中exchange()之前的操作发生在另一个线程中相应exchange()之后的操作之前。
 *     </li>
 *     <li>
 *         调用CyclicBarrier.await和Phaser.awaitAdvance （以及它的变体）之前的动作由屏障动作执行的动作发生之前发生的动作，
 *         以及由屏障动作执行的动作发生之前发生的动作从其他相应等待成功await之后的动作线程。
 *     </li>
 * </ul>
 *
 * @author xuMingHai
 */
public class VolatileDemo05 {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(VolatileDemo05.class);


	public static void main(String[] args) {

		// 原子性测试
		Demo1.test();

		// 防止指令重排序测试
		Demo2.getInstance().printMessage();

	}


	/**
	 * 原子性测试（一个线程写，多个线程读）
	 */
	private static class Demo1 {

		/**
		 * 多个线程的共享资源，添加volatile确保可见性
		 */
		private volatile static long value;

		public static void test() {
			// 线程1写
			final Thread t1 = new Thread(() -> value = 10);

			// 线程2读
			final Thread t2 = new Thread(() -> LOGGER.info("value = {}", value));

			// 线程3读
			final Thread t3 = new Thread(() -> LOGGER.info("value = {}", value));

			// 启动线程
			t1.start();
			t2.start();
			t3.start();

			// 主线程读
			LOGGER.info("value = {}", value);

		}

	}

	/**
	 * 防止指令重排序
	 */
	private static class Demo2 {

		/**
		 * 添加volatile确保在赋值的时候不会先获取值后再赋值
		 */
		private volatile static Instance instance;

		public static Instance getInstance() {
			// 先不加锁判断是否为null
			if (instance == null) {
				// 获取锁之后再次判断，如果没有其他线程已经创建了实例对象就创建
				synchronized (Demo2.class) {
					if (instance == null) {
						instance = new Instance();
					}
				}
			}
			return instance;
		}

		private static class Instance {

			public void printMessage() {
				LOGGER.info("Hello Word!");
			}
		}

	}

}

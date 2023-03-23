package xyz.xuminghai.atomic;

import java.util.concurrent.atomic.AtomicMarkableReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2023/3/20 21:39 星期一<br/>
 *
 * <h1>可标记的原子引用示例</h1>
 * 使用标记记录原子引用的修改情况，让原子引用更富有操作性
 *
 * @author xuMingHai
 */
public class AtomicMarkReferenceDemo {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AtomicMarkReferenceDemo.class);


	public static void main(String[] args) {

		// 带有标记的原子引用，经过减法后标记为 false
		final AtomicMarkableReference<Integer> markReference = new AtomicMarkableReference<>(0, true);

		Thread t1 = new Thread(() -> {
			// 自增，存在减法放弃自增
			while (markReference.isMarked()) {
				Integer expectedReference = markReference.getReference();
				Integer newReference = expectedReference + 1;
				if (markReference.compareAndSet(expectedReference,
						newReference, true, true)) {
					LOGGER.info("不存在减法，自增成功");
					return;
				}
			}
			LOGGER.info("存在减法，取消自增");
		}, "t1");

		Thread t2 = new Thread(() -> {
			// 自减，存在减法放弃自减
			while (markReference.isMarked()) {
				Integer expectedReference = markReference.getReference();
				Integer newReference = expectedReference - 1;
				if (markReference.compareAndSet(expectedReference,
						newReference, true, false)) {
					LOGGER.info("不存在减法，自减成功");
					return;
				}
			}
			LOGGER.info("已存在减法，取消自减");
		}, "t2");

		Thread t3 = new Thread(() -> {
			// 只有存在减法后才会自减
			while (!markReference.isMarked()) {
				Integer expectedReference = markReference.getReference();
				Integer newReference = expectedReference - 1;
				if (markReference.compareAndSet(expectedReference,
						newReference, false, false)) {
					LOGGER.info("存在减法，自减成功");
					return;
				}
			}
			LOGGER.info("不存在减法，取消自减");
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

		LOGGER.info("reference = {}, mark = {}", markReference.getReference(),
				markReference.isMarked());

	}

}

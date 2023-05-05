package xyz.xuminghai.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;
import java.util.concurrent.locks.StampedLock;

/**
 * 2023/4/9 12:25 星期日<br/>
 * <h1>StampedLock示例1</h1>
 * 具有三种控制读/写访问的模式。 StampedLock 的状态由版本和模式组成。是不可重入。<br/>
 * StampedLocks 设计用作线程安全组件开发中的内部实用程序。
 * 它们的使用依赖于对它们所保护的数据、对象和方法的内部属性的了解。
 *
 * @author xuMingHai
 */
public class StampedLockDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StampedLockDemo01.class);


    public static void main(String[] args) {
        Point point = new Point();

        Thread t1 = new Thread(() -> LOGGER.info("Point.distanceFromOrigin = {}", point.distanceFromOrigin()), "t1");

        Thread t2 = new Thread(() -> {
            double newX = 2.5, newY = 3.8;
            LOGGER.info("Point.moveIfAtOrigin({}, {})", newX, newY);
            point.moveIfAtOrigin(newX, newY);
        }, "t2");

        Thread t3 = new Thread(() -> {
            double newX = 3.8, newY = 2.5;
            LOGGER.info("Point.moveIfAtOrigin({}, {})", newX, newY);
            point.moveIfAtOrigin(newX, newY);
        }, "t3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Point = {}", point);

    }


    private static class Point {
        private final StampedLock stampedLock = new StampedLock();
        private double x, y;

        void move(double deltaX, double deltaY) {
            // 获取独占锁
            long stamp = stampedLock.writeLock();
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                // 释放独占锁
                stampedLock.unlockWrite(stamp);
            }
        }

        double distanceFromOrigin() {
            // 乐观读
            long stamp = stampedLock.tryOptimisticRead();
            // 保持并发值到栈帧中
            double currentX = x, currentY = y;
            // 验证数据是否有效
            if (!stampedLock.validate(stamp)) { // 数据无效，使用共享锁
                stamp = stampedLock.readLock();
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            // 在直角坐标系中，根据勾股定理计算直角三角形斜边长度
            return Math.hypot(currentX, currentY);
        }

        void moveIfAtOrigin(double newX, double newY) {
            // 使用乐观读
            long stamp = stampedLock.tryOptimisticRead();
            double currentX = x, currentY = y;
            if (stampedLock.validate(stamp)) {
                if (currentX == 0.0 && currentY == 0.0) {
                    try {
                        long ws = stampedLock.tryConvertToWriteLock(stamp);
                        if (ws != 0L) {
                            stamp = ws;
                            x = newX;
                            y = newY;
                            return;
                        }
                    } finally {
                        stampedLock.unlock(stamp);
                    }
                } else {
                    return;
                }
            }
            // 使用读锁
            stamp = stampedLock.readLock();
            try {
                while (x == 0.0 && y == 0.0) {
                    long ws = stampedLock.tryConvertToWriteLock(stamp);
                    if (ws != 0L) {
                        stamp = ws;
                        x = newX;
                        y = newY;
                        break;
                    } else {
                        stampedLock.unlockRead(stamp);
                        stamp = stampedLock.writeLock();
                    }
                }
            } finally {
                stampedLock.unlock(stamp);
            }
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Point.class.getSimpleName() + "[", "]")
                    .add("x=" + x)
                    .add("y=" + y)
                    .toString();
        }
    }

}

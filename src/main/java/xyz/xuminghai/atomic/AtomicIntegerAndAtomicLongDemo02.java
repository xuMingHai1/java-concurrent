package xyz.xuminghai.atomic;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2022/3/7 11:25 星期一<br/>
 * <h1>AtomicInteger和AtomicLong原理分析</h1>
 * AtomicInteger本质使用的是{@link Unsafe}，利用CAS机制，比较期望值和实际值，成功的话则将实际值替换为更新值
 * @author xuMingHai
 */
public class AtomicIntegerAndAtomicLongDemo02 {


    public static void main(String[] args) {
        final SimpleAtomicInteger simpleAtomicInteger = new SimpleAtomicInteger();
        



    }

    /**
     * 简单的原子整型实现类
     */
    private static class SimpleAtomicInteger {
        /**
         * 用于执行不安全方法的集合
         */
        private static final Unsafe UNSAFE = Unsafe.getUnsafe();

        /**
         * value字段的偏移量，同一类的不同实例的字段偏移量是相同的
         */
        private static final long VALUE_OFFSET;

        /*
            使用unsafe获取value字段的偏移量
         */
        static {
            try {
                VALUE_OFFSET = UNSAFE.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
            } catch (Exception ex) {
                throw new Error(ex);
            }
        }

        private volatile int value;

        /**
         * 获取当前值，因为value是使用volatile修饰的
         * @return value
         */
        public int get() {
            return value;
        }

        /**
         * 将预期值和实际值作比较，如果没有线程已经修改了，那么将值改为要更新的值
         * @param expect 预期值
         * @param update 更新值
         * @return true表示更新成功，false更新失败
         */
        public boolean completeAndSet(int expect, int update) {
            return UNSAFE.compareAndSwapInt(this, VALUE_OFFSET, expect, update);
        }



    }


}

package xyz.xuminghai.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.L_Result;

import java.io.Serializable;

/**
 * 2022/5/8 14:47 星期日<br/>
 * 双重检查锁单例<br/>
 * <p>在维基百科中的<a href="https://zh.m.wikipedia.org/zh-cn/%E5%8F%8C%E9%87%8D%E6%A3%80%E6%9F%A5%E9%94%81%E5%AE%9A%E6%A8%A1%E5%BC%8F">双重检查锁</a>，使用了局部变量代替直接比较 volatile 修饰的变量</p>
 * 以下来自 Joshua Bloch "Effective Java, Second Edition", p. 283
 *  <pre>
 * //Double-check idiom for lazy initialization of instance fields
 * private volatile FieldType field;
 * Fie1dType getFie1d() {
 *             FieldType result= field;
 *             if(result == nu11) { // First check (no locking)
 *                 synchronized(this) {
 *                     result = field;
 *                     if (result == nul1) // Second check (with locking)
 *                         field = result = computeFieldValue();
 *                 }
 *             }
 *             return result;
 *         }
 *  </pre>
 *  这段代码可能看起来似乎有些费解。尤其对于需要用到局部变量result可能有点不解。
 *  这个变量的作用是确保field只在已经被初始化的情况下读取一次。虽然这不是严格需要，但是可以提升性能，并且因为给低级的并发编程应用了一些标准，因此更加优雅。
 *  在我的机器上，上述的方法比没用局部变量的方法快了大约25%。
 * @author xuMingHai
 */
@JCStressTest
@Outcome(id = "Instance", expect = Expect.ACCEPTABLE, desc = "OK")
@Outcome(expect = Expect.ACCEPTABLE_INTERESTING, desc = "INTERESTING!!!")
@State
public class DoubleCheckLockTest {

    private volatile static Instance instance;

    public static Instance getInstance() {
        Instance result = instance;
        if (result == null) {
            synchronized (DoubleCheckLockTest.class) {
                result = instance;
                if (result == null) {
                    instance = new Instance();
                }
            }
        }
        return instance;
    }

    private static class Instance implements Serializable {

        private static final long serialVersionUID = -3949564844175144101L;

        @Override
        public String toString() {
            return "Instance";
        }
    }

        /*
      使用4个线程测试
     */

    @Actor
    public void actor1(L_Result r) {
        r.r1 = getInstance();
    }

    @Actor
    public void actor2(L_Result r) {
        r.r1 = getInstance();
    }

    @Actor
    public void actor3(L_Result r) {
        r.r1 = getInstance();
    }

    @Actor
    public void actor4(L_Result r) {
        r.r1 = getInstance();
    }


}

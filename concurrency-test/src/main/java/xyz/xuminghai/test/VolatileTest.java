package xyz.xuminghai.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

/**
 * 2022/3/5 20:10 星期六<br/>
 * <h1>volatile指令重排序和happen-before测试</h1>
 * 什么情况下有happen-before：
 * <ol>
 *     <li>单线程情况下</li>
 *     <li>synchronized</li>
 *     <li>volatile</li>
 *     <li>final</li>
 * </ol>
 *
 * <p color='red'>注意desc不支持中文！！！</p>
 * @author xuMingHai
 */
@JCStressTest
@Outcome(id = {"1", "2"}, expect = Expect.ACCEPTABLE, desc = "OK")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "INTERESTING!!!")
@State
public class VolatileTest {

    /**
     * 多个线程共享的值
     */
    int i;

    /**
     * 添加volatile 对其修改后立即可见
     */
    volatile boolean flag;

    /*
        添加volatile后，无论这两个线程谁先运行，结果都不会出现0
     */

    @Actor
    public void actor1(I_Result r) {
        // 在没有volatile修饰的情况下，期望只会出现1或者2，但是还有可能因为指令重排的原因，i = 0;
        if (flag) {
            r.r1 = i + i;
        } else {
            r.r1 = 1;
        }

    }

    /**
     * 在volatile修饰 flag 情况下，如果线程2先运行，线程1的判断为真，一定会出现2<br/>
     * 如果线程1先运行，那么线程1的判断为假，一定会出现1<br/><br/>
     * 在没有volatile修饰 flag 情况下，如果线程2先运行，则可能会出现执行重排序，先运行 flag = true
     * 后赋值 i = 1，然后这时线程1启动了，获取到的i还是0，所以会出现结果是0
     */
    @Actor
    public void actor2() {
        // 由于happen-before，i=1 一定是可见的
        i = 1;
        // 可能因为重排序原因这个在前面运行，但是volatile和JMM（java内存模型）的happen-before 之前的可见性
        flag = true;


    }


}

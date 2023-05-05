package xyz.xuminghai.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.L_Result;

import java.io.Serializable;

/**
 * 2022/5/8 15:01 星期日<br/>
 * 和{@link DoubleCheckLockTest}比较
 *
 * @author xuMingHai
 */
@JCStressTest
@Outcome(id = "Instance", expect = Expect.ACCEPTABLE, desc = "OK")
@Outcome(expect = Expect.ACCEPTABLE_INTERESTING, desc = "INTERESTING!!!")
@State
public class CompareDoubleCheckLockTest {

    private volatile static Instance instance;

    public static Instance getInstance() {
        Instance result = instance;
        if (result == null) {
            synchronized (CompareDoubleCheckLockTest.class) {
                result = instance;
                if (result == null) {
                    instance = new Instance();
                }
            }
        }
        return instance;
    }

    @Actor
    public void actor1(L_Result r) {
        r.r1 = getInstance();
    }

    /*
      使用4个线程测试
     */

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

    private static class Instance implements Serializable {
        private static final long serialVersionUID = -3475949357133835998L;

        @Override
        public String toString() {
            return "Instance";
        }
    }

}

package xyz.xuminghai.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.IIII_Result;

/**
 * 2022/3/6 2:44 星期日<br/>
 * <h1>final的happen-before测试</h1>
 * <p color='red'>注意多个结果值的逗号中间要加空格</p>
 * @author xuMingHai
 */
@JCStressTest
@Outcome(id = "-1, -1, -1, -1", expect = Expect.ACCEPTABLE, desc = "Object is not seen yet.")
@Outcome(id = "1, 2, 3, 4", expect = Expect.ACCEPTABLE, desc = "Seen the complete object.")
@Outcome(expect = Expect.ACCEPTABLE_INTERESTING, desc = "Seeing partially constructed object!!!")
@State
public class FinalTest {

	int v = 1;

	MyObject o;

	@Actor
	public void actor1() {
		o = new MyObject(v);
	}

	@Actor
	public void actor2(IIII_Result r) {
		MyObject o = this.o;
		if (o != null) {
			r.r1 = o.x1;
			r.r2 = o.x2;
			r.r3 = o.x3;
			r.r4 = o.x4;
		}
		else {
			r.r1 = -1;
			r.r2 = -1;
			r.r3 = -1;
			r.r4 = -1;
		}
	}

	public static class MyObject {
		int x1, x2, x3, x4;

		public MyObject(int v) {
			x1 = v;
			x2 = x1 + v;
			x3 = x2 + v;
			x4 = x3 + v;
		}
	}

}

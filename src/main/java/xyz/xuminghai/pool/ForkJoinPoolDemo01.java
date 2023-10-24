package xyz.xuminghai.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 2023/10/13 14:24 星期五<br/>
 *
 * @author xuMingHai
 */
public class ForkJoinPoolDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ForkJoinPoolDemo01.class);

    public static void main(String[] args) {
        // 单线程执行
        SingleThreadFindPrime singleThreadFindPrime = new SingleThreadFindPrime();
        singleThreadFindPrime.countPrime(30_000_000);

        // 全部可用核心数执行（每个线程执行的任务量大致相同）
        ConcurrentFindPrime concurrentFindPrime = new ConcurrentFindPrime();
        concurrentFindPrime.countPrime(30_000_000);

        // ForkJoin递归任务（减少了计算平均任务量）
        ForkJoinFindPrime forkJoinFindPrime = new ForkJoinFindPrime();
        forkJoinFindPrime.countPrime(30_000_000);
    }

    static abstract class AbstractFindPrime {
        public void countPrime(int maxValue) {
            long startTime = System.nanoTime();
            LOGGER.info("executor = {}, maxValue = {}, 查找到素数 = {}，耗时 = {}ms",
                    this.getClass().getSimpleName(), maxValue,
                    find(maxValue), (System.nanoTime() - startTime) / 1_000_000);
        }

        protected abstract int find(int maxValue);

        protected int findPrime(int minValue, int maxValue) {
            // 从5开始
            int count = 0;
            if (minValue < 4) {
                for (int i = minValue; i < 4; i++) {
                    int j = 2;
                    for (; j < i; j++) {
                        if (i % j == 0) {
                            break;
                        }
                    }
                    if (j == i) {
                        count++;
                    }
                }
                minValue = 5;
            }
            else {
                // 不是奇数加1
                if ((minValue & 1) != 1) {
                    ++minValue;
                }

            }
            // 用于满足 i < maxValue
            maxValue++;
            // 除2之外的素数都是奇数
            for (int i = minValue; i < maxValue; i += 2) {
                // 简化被除数
                int j = (int) Math.sqrt(i);
                // 满足 k < j
                j++;
                // 被除数
                int k = 3;
                for (; k < j; k++) {
                    // 可以被中间的数整除结束查找
                    if (i % k == 0) {
                        break;
                    }
                }
                // 符合条件素数条件
                if (j == k) {
                    count++;
                }
            }
            return count;
        }

    }

    /**
     * 单线程查找素数
     */
    static class SingleThreadFindPrime extends AbstractFindPrime {

        @Override
        protected int find(int maxValue) {
            return super.findPrime(2, maxValue);
        }

    }

    /**
     * 并发查找素数（根据计算机核心数拆分）
     */
    static class ConcurrentFindPrime extends AbstractFindPrime {

        @Override
        protected int find(int maxValue) {
            if (maxValue < 1_000_000) {
                return super.findPrime(2, maxValue);
            }
            int primeCount = 0;
            // 可用处理器
            int processor = Runtime.getRuntime().availableProcessors();
            // 使用可用处理器线程池并发执行
            ExecutorService executorService = Executors.newFixedThreadPool(processor);
            // 拆分任务，越大的数所需要查找的被除数越多
            // 并行任务
            List<Callable<Integer>> list = createTaskList(maxValue, processor);
            try {
                // 执行并行任务
                List<Future<Integer>> futureList = executorService.invokeAll(list);
                // 统计结果
                for (Future<Integer> future : futureList) {
                    primeCount += future.get();
                }
            }
            catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            // 关闭线程池
            executorService.shutdown();

            return primeCount;
        }

        private List<Callable<Integer>> createTaskList(int maxValue, int processor) {
            List<Callable<Integer>> list = new ArrayList<>(processor);
            long count = 0;
            // 从2开始计算
            int lastValue = 1;
            long avg = avg(lastValue + 1, maxValue, processor);
            for (int i = lastValue; i < maxValue; i++) {
                count += i;
                // 到达平均值时添加任务
                if (count >= avg) {
                    // lambda final变量
                    int finalI = i;
                    int finalLastValue = lastValue;
                    // 最后一份计算剩下的部分
                    if (processor - list.size() == 1) {
                        list.add(() -> ConcurrentFindPrime.super.findPrime(finalLastValue + 1, maxValue));
                        break;
                    }
                    // 添加平均任务（任务量差距教小）
                    list.add(() -> ConcurrentFindPrime.super.findPrime(finalLastValue + 1, finalI));
                    // 统计归零
                    count = 0;
                    // 上一次的值
                    lastValue = i;
                    // 再次计算平均值
                    avg = avg(lastValue + 1, maxValue, processor);
                }
            }
            return list;
        }

        private long avg(int minValue, int maxValue, int unit) {
            long count = 0;
            maxValue++;
            for (int i = minValue; i < maxValue; i++) {
                count += i;
            }
            return count / unit;
        }
    }


    /**
     * ForkJoinTask查找
     */
    static class ForkJoinFindPrime extends AbstractFindPrime {

        /**
         * 估计每个任务的计算量(会严重影响ForkJoinTask执行时间)
         */
        static int part = 1_000_000;

        @Override
        protected int find(int maxValue) {
            ForkJoinTask<Integer> forkJoinTask = ForkJoinPool.commonPool().submit(new FindPrimeTask(maxValue - part, maxValue));
            return forkJoinTask.join();
        }

        class FindPrimeTask extends RecursiveTask<Integer> {

            private final int minValue;

            private final int maxValue;

            public FindPrimeTask(int minValue, int maxValue) {
                this.minValue = minValue;
                this.maxValue = maxValue;
            }

            @Override
            protected Integer compute() {
                if (minValue <= part) {
                    return ForkJoinFindPrime.super.findPrime(2, maxValue);
                }
                FindPrimeTask findPrimeTask = new FindPrimeTask(minValue - part - 1, minValue - 1);
                // 先安排异步执行，再进行计算
                findPrimeTask.fork();
                int count = ForkJoinFindPrime.super.findPrime(minValue, maxValue);
                return count + findPrimeTask.join();
            }
        }
    }

}


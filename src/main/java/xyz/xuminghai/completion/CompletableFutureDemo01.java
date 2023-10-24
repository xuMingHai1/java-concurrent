package xyz.xuminghai.completion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 2023/10/23 12:51 星期一<br/>
 * 可能异步计算的一个阶段，当另一个 CompletionStage 完成时执行操作或计算值。
 * 阶段在其计算终止时完成，但这可能反过来触发其他依赖阶段。
 * 简化多线程编程的复杂性
 *
 * @author xuMingHai
 */
public class CompletableFutureDemo01 {

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompletableFutureDemo01.class);

    public static void main(String[] args) {
        String string;
        try {
            // 异步分阶段任务
            string = CompletableFuture.runAsync(() -> LOGGER.info(Thread.currentThread().getName()))
                    .thenRunAsync(() -> LOGGER.info("异步执行任务"))
                    .thenRun(() -> LOGGER.info("任务完成"))
                    .thenCombine(CompletableFuture.completedFuture("Ok"), (unused, s) -> s)
                    .get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info(string);

        // 串行任务
        CompletableFuture.completedFuture("newValue")
                .thenAccept(LOGGER::info)
                .thenRun(() -> LOGGER.info("串行任务执行完成"));
    }

}

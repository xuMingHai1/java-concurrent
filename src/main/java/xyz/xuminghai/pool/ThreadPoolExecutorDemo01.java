package xyz.xuminghai.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 2023/10/2 17:34 星期一<br/>
 * {@link ExecutorService} 的实现之一。
 * 线程池解决了两个不同的问题：由于减少了每个任务的调用开销，
 * 它们通常在执行大量异步任务时提供改进的性能，并且它们提供了一种限制和管理资源的方法，
 * 包括执行一组异步任务时消耗的线程。任务。每个ThreadPoolExecutor还维护一些基本统计数据，例如已完成任务的数量。
 * 为了在广泛的上下文中发挥作用，此类提供了许多可调整的参数和可扩展性挂钩。但是，建议程序员使用更方便的Executors工厂方法Executors.newCachedThreadPool （无界线程池，具有自动线程回收功能）、 Executors.newFixedThreadPool （固定大小线程池）和Executors.newSingleThreadExecutor （单后台线程），这些方法可以预先配置设置最常见的使用场景。否则，请在手动配置和调整此类时使用以下指南：
 * <p>核心和最大池大小</p>
 * ThreadPoolExecutor将根据 corePoolSize （请参阅getPoolSize ）和 MaximumPoolSize （请参阅getMaximumPoolSize ）设置的边界自动调整池大小（请getCorePoolSize getPoolSize ）。当在方法execute(Runnable)中提交新任务并且正在运行的线程少于corePoolSize时，即使其他工作线程处于空闲状态，也会创建一个新线程来处理该请求。如果运行的线程数大于 corePoolSize 但小于 maxPoolSize，则仅当队列已满时才会创建新线程。通过将 corePoolSize 和 MaximumPoolSize 设置为相同，您可以创建固定大小的线程池。通过将 MaximumPoolSize 设置为本质上无界的值（例如Integer.MAX_VALUE ，您可以允许池容纳任意数量的并发任务。最典型的是，核心和最大池大小仅在构造时设置，但也可以使用setCorePoolSize和setMaximumPoolSize动态更改。
 * <p>按需施工</p>
 * 默认情况下，即使是核心线程也仅在新任务到达时才最初创建和启动，但这可以使用方法prestartCoreThread或prestartAllCoreThreads动态覆盖。如果您使用非空队列构造池，您可能需要预启动线程。
 * <p>创建新线程</p>
 * 新线程是使用ThreadFactory创建的。如果没有另外指定，则使用 E Executors.defaultThreadFactory ，它创建的线程都位于同一ThreadGroup中，并具有相同的NORM_PRIORITY优先级和非守护进程状态。通过提供不同的 ThreadFactory，您可以更改线程的名称、线程组、优先级、守护进程状态等。如果ThreadFactory在通过从newThread返回 null 进行询问时未能创建线程，则执行程序将继续，但可能无法执行任何任务。线程应该拥有“modifyThread” RuntimePermission 。如果工作线程或使用该池的其他线程不拥有此权限，则服务可能会降级：配置更改可能无法及时生效，并且关闭池可能仍处于可能终止但未完成的状态。
 * <p>保活次数</p>
 * 如果池当前拥有超过 corePoolSize 的线程，则多余的线程如果空闲时间超过 keepAliveTime （请参阅getKeepAliveTime(TimeUnit) ），将被终止。这提供了一种在池未被积极使用时减少资源消耗的方法。如果池稍后变得更加活跃，则会构造新线程。还可以使用方法setKeepAliveTime(long, TimeUnit)动态更改此参数。使用Long.MAX_VALUE TimeUnit.NANOSECONDS值可以有效地禁止空闲线程在关闭之前终止。默认情况下，仅当线程数超过 corePoolSize 时才应用保持活动策略。但是，只要 keepAliveTime 值非零，也可以使用方法allowCoreThreadTimeOut(boolean)将此超时策略应用于核心线程。
 * <p>排队</p>
 * 任何BlockingQueue都可用于传输和保存已提交的任务。该队列的使用与池大小相互作用：
 * 如果运行的线程少于 corePoolSize，则 Executor 始终倾向于添加新线程而不是排队。
 * 如果 corePoolSize 或更多线程正在运行，Executor 总是更喜欢对请求进行排队而不是添加新线程。
 * 如果请求无法排队，则会创建一个新线程，除非这超出了 MaximumPoolSize，在这种情况下，该任务将被拒绝。
 * 排队的一般策略有以下三种：
 * 直接交接。工作队列的一个不错的默认选择是SynchronousQueue ，它将任务交给线程而不以其他方式保留它们。在这里，如果没有线程可以立即运行任务，则尝试对任务进行排队将会失败，因此将构造一个新线程。此策略可避免在处理可能具有内部依赖性的请求集时发生锁定。直接切换通常需要无限制的 MaximumPoolSizes 以避免拒绝新提交的任务。这反过来又承认当命令到达的平均速度继续快于处理速度时，线程可能会无限增长。
 * 无界队列。当所有 corePoolSize 线程都忙时，使用无界队列（例如没有预定义容量的LinkedBlockingQueue ）将导致新任务在队列中等待。因此，将不会创建超过 corePoolSize 的线程。 （因此，maximumPoolSize 的值不会产生任何影响。）当每个任务完全独立于其他任务时，这可能是合适的，因此任务不会影响彼此的执行；例如，在网页服务器中。虽然这种排队方式对于平滑请求的瞬时突发很有用，但它承认当命令平均到达速度继续快于处理速度时，工作队列可能会无限增长。
 * 有界队列。有界队列（例如ArrayBlockingQueue ）在与有限的 MaximumPoolSizes 一起使用时有助于防止资源耗尽，但可能更难以调整和控制。队列大小和最大池大小可以相互权衡：使用大队列和小池可以最大限度地减少 CPU 使用率、操作系统资源和上下文切换开销，但可能会导致人为降低吞吐量。如果任务经常阻塞（例如，如果它们受 I/O 限制），系统可能会为比您允许的更多线程安排时间。使用小队列通常需要更大的池大小，这会使 CPU 更加繁忙，但可能会遇到不可接受的调度开销，这也会降低吞吐量。
 * <p>拒绝的任务</p>
 * 当 Executor 关闭时，以及当 Executor 对最大线程和工作队列容量使用有限界限并且饱和时，在方法execute(Runnable)中提交的新任务将被拒绝。无论哪种情况， execute方法都会调用其RejectedExecutionHandler的RejectedExecutionHandler.rejectedExecution(Runnable, ThreadPoolExecutor)方法。提供了四种预定义的处理程序策略：
 * 在默认的ThreadPoolExecutor.AbortPolicy中，处理程序在拒绝时抛出运行时RejectedExecutionException 。
 * 在ThreadPoolExecutor.CallerRunsPolicy中，调用execute本身的线程运行任务。这提供了一个简单的反馈控制机制，将减慢新任务提交的速度。
 * 在ThreadPoolExecutor.DiscardPolicy中，无法执行的任务将被简单地删除。
 * 在ThreadPoolExecutor.DiscardOldestPolicy中，如果执行器没有关闭，则工作队列头部的任务将被丢弃，然后重试执行（可能会再次失败，导致重复执行）。
 * 可以定义和使用其他类型的RejectedExecutionHandler类。这样做需要小心，特别是当策略设计为仅在特定容量或排队策略下工作时。
 * <p>钩子方法</p>
 * 此类提供protected可重写beforeExecute(Thread, Runnable)和afterExecute(Runnable, Throwable)方法，这些方法在每个任务执行之前和之后调用。这些可用于操纵执行环境；例如，重新初始化 ThreadLocals、收集统计信息或添加日志条目。此外，一旦执行器完全terminated方法来执行需要完成的任何特殊处理。
 * 如果钩子或回调方法抛出异常，内部工作线程可能会失败并突然终止。
 * <p>队列维护</p>
 * 方法getQueue()允许访问工作队列以进行监视和调试。强烈建议不要将此方法用于任何其他目的。当大量排队任务被取消时， remove(Runnable)和purge这两个提供的方法可用于协助存储回收。
 * <p>最终确定</p>
 * 程序中不再引用且没有剩余线程的池将自动shutdown 。如果您想确保即使用户忘记调用shutdown也能回收未引用的池，那么您必须通过设置适当的保持活动时间、使用零核心线程的下限和/或设置allowCoreThreadTimeOut(boolean)来安排未使用的线程最终死亡allowCoreThreadTimeOut(boolean) 。
 *
 * @author xuMingHai
 */
public class ThreadPoolExecutorDemo01 {


    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolExecutorDemo01.class);


    /**
     * 最大为10个线程，任务队列容量为5的线程池
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 10, 5L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5));


    public static void main(String[] args) {
        // 测试是否会抛出拒绝提交任务异常
        for (int i = 0; i < 16; i++) {
            try {
                THREAD_POOL_EXECUTOR.execute(() -> {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (RejectedExecutionException e) {
                LOGGER.error(e.getMessage());
            }

        }

        LOGGER.info(THREAD_POOL_EXECUTOR.toString());
        THREAD_POOL_EXECUTOR.shutdown();

    }

}

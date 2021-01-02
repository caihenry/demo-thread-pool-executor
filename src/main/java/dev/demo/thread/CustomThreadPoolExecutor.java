package dev.demo.thread;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
 
public class CustomThreadPoolExecutor extends ThreadPoolExecutor {
 
    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * https://stackoverflow.com/questions/10934187/how-to-wait-for-a-threadpoolexecutor-to-finish
     * https://stackoverflow.com/questions/3332832/graceful-shutdown-of-threads-and-executor
     * 
     */
    @Override
    public void shutdown() {
        super.shutdown();
        try {
            if (!this.awaitTermination(120, TimeUnit.SECONDS)) { //optional *
                System.out.println("Executor did not terminate in the specified time."); //optional *
                List<Runnable> droppedTasks = this.shutdownNow(); //optional **
                System.out.println("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed."); //optional **
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.println("Perform beforeExecute() logic, name: " + ((DemoTask)r).getName() + ".");
    }
 
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            System.out.println("Perform exception handler logic, name: " + ((DemoTask)r).getName() + ".");
        }
        System.out.println("Perform afterExecute() logic, name: " + ((DemoTask)r).getName() + ".");
    }
 
}
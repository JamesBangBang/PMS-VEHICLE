package com.starnetsecurity.common.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExecutor implements Executor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SemaphoreExecutor.class);

    private int size = 0;
    private String threadName;
    private Semaphore semaphore;

    public SemaphoreExecutor() {
    }

    public SemaphoreExecutor(int size, String threadName) {
        this.size = size;
        this.threadName = threadName;
        this.semaphore = new Semaphore(this.size, true);
    }

    @Override
    public void execute(Runnable command) {
        long now = System.currentTimeMillis();
        try {
            this.semaphore.tryAcquire(3, TimeUnit.MINUTES);
            command.run();
            this.semaphore.release();
        } catch (InterruptedException e) {
            LOGGER.error(this.threadName + "：线程等待超时，无法执行", e);
        } finally {
            LOGGER.debug(this.threadName + "：线程执行结束，耗时：{}ms", System.currentTimeMillis() - now);
        }
    }
}

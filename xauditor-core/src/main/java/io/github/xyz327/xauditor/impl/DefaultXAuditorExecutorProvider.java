package io.github.xyz327.xauditor.impl;

import io.github.xyz327.xauditor.XAuditorExecutorProvider;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xizhou
 * @date 2021/6/1 20:43
 */
public class DefaultXAuditorExecutorProvider implements XAuditorExecutorProvider {
    @Override
    public Executor getExecutor() {
        return new ThreadPoolExecutor(10, 64, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000), new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("xauditor-exec-"+count.incrementAndGet());
                return thread;
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}

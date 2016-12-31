package me.shawlaf.banmanager.async;

import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import me.shawlaf.banmanager.Banmanager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Florian on 31.12.2016.
 */
public class Multithreading {
    
    private Multithreading() {}
    
    private static ExecutorService POOL;
    
    public static void initialize(Banmanager banmanager) {
        POOL = banmanager.getExecutorService();
    }
    
    public static void runAsync(Runnable runnable) {
        POOL.execute(runnable);
    }
    
}

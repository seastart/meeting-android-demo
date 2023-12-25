package com.freewind.seastarvideo.utils;


import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yawei
 * @ description 线程池工具类
 */
public class AppExecutors {

    private static final String TAG = "AppExecutors";
    /**磁盘IO线程池**/
    private final ExecutorService diskIO;
    /**网络IO线程池**/
    private final ExecutorService networkIO;
    /**普通线程池**/
    private final ExecutorService normalThreads;
    /**UI线程**/
    private final Executor mainThread;
    /**定时任务线程池**/
    private final ScheduledExecutorService scheduledExecutor;

    private volatile static AppExecutors appExecutors;

    public static AppExecutors getInstance() {
        if (appExecutors == null) {
            synchronized (AppExecutors.class) {
                if (appExecutors == null) {
                    appExecutors = new AppExecutors();
                }
            }
        }
        return appExecutors;
    }

    public AppExecutors(ExecutorService diskIO, ExecutorService networkIO,
                        Executor mainThread, ScheduledExecutorService scheduledExecutor, ExecutorService normalThreads) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
        this.scheduledExecutor = scheduledExecutor;
        this.normalThreads = normalThreads;
    }

    public AppExecutors() {
        this(diskIoExecutor(), networkExecutor(), new MainThreadExecutor(), scheduledThreadPoolExecutor(), normalExecutor());
    }
    /**
     * 定时(延时)任务线程池
     * 替代Timer,执行定时任务,延时任务
     */
    public ScheduledExecutorService scheduledExecutor() {
        return scheduledExecutor;
    }

    /**
     * 磁盘IO线程池（单线程）
     * 和磁盘操作有关的进行使用此线程(如读写数据库,读写文件)
     * 禁止延迟,避免等待
     * 此线程不用考虑同步问题
     */
    public ExecutorService diskIO() {
        return diskIO;
    }
    /**
     * 网络IO线程池
     * 网络请求,异步任务等适用此线程
     * 不建议在这个线程 sleep 或者 wait
     */
    public ExecutorService networkIO() {
        return networkIO;
    }

    /**
     * 普通线程池
     * 网络请求,异步任务等适用此线程
     * 不建议在这个线程 sleep 或者 wait
     */
    public ExecutorService normalThreads() {
        return normalThreads;
    }

    /**
     * UI线程
     * Android 的MainThread
     * UI线程不能做的事情这个都不能做
     */
    public Executor mainThread() {
        return mainThread;
    }

    private static ScheduledExecutorService scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(16, r -> new Thread(r, "scheduled_executor"), (r, executor) -> LogUtil.e(TAG, "rejectedExecution: scheduled executor queue overflow"));
    }

    private static ExecutorService diskIoExecutor() {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), r -> new Thread(r, "disk_executor"), (r, executor) -> LogUtil.e(TAG, "rejectedExecution: disk io executor queue overflow"));
    }

    private static ExecutorService networkExecutor() {
        return new ThreadPoolExecutor(3, 6, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(6), r -> new Thread(r, "network_executor"), (r, executor) -> LogUtil.e(TAG, "rejectedExecution: network executor queue overflow"));
    }

    private static ExecutorService normalExecutor() {
        return new ThreadPoolExecutor(3, 5, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(11), r -> new Thread(r, "normal_executor"), (r, executor) -> LogUtil.e(TAG, "rejectedExecution: normal executor queue overflow"));
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}



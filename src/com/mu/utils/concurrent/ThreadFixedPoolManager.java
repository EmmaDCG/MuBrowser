package com.mu.utils.concurrent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public enum ThreadFixedPoolManager {
   POOL_MAPWORK(10, "mapwork-thread"),
   POOL_OTHER(5, "other-thread"),
   POOL_FINDWAY(5, "findWay-thread");

   private int count;
   private String name;
   private ScheduledThreadPoolExecutor executor;

   private ThreadFixedPoolManager(int count, String name) {
      this.count = count;
      this.name = name;
      this.executor = new ScheduledThreadPoolExecutor(count);
      ThreadFactoryImpl factory = new ThreadFactoryImpl(this.executor, name);
      this.executor.setThreadFactory(factory);
      this.executor.setRejectedExecutionHandler(new ServerRejectedExecutionHandler());
      this.executor.prestartAllCoreThreads();
   }

   public int getCount() {
      return this.count;
   }

   public String getName() {
      return this.name;
   }

   public ScheduledThreadPoolExecutor getScheduledExecutor() {
      return this.executor;
   }

   public final ScheduledFuture schedule(Runnable r, long delay) {
      Runnable r2 = new RunWrapper(r, true);
      return this.executor.schedule(r2, delay, TimeUnit.MILLISECONDS);
   }

   public final ScheduledFuture execute(Runnable r) {
      Runnable r2 = new RunWrapper(r, true);
      return this.executor.schedule(r2, 0L, TimeUnit.MILLISECONDS);
   }

   public final ScheduledFuture scheduleTask(Runnable r, long delay, long period) {
      Runnable r2 = new RunWrapper(r, false);
      return this.executor.scheduleAtFixedRate(r2, delay, period, TimeUnit.MILLISECONDS);
   }

   public final void purge() {
      this.executor.purge();
   }
}

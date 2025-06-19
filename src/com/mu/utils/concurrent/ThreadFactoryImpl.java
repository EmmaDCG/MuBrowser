package com.mu.utils.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryImpl implements ThreadFactory {
   private String name;
   private AtomicInteger threadNumber = new AtomicInteger(1);
   private ThreadGroup group;

   public ThreadFactoryImpl(ThreadPoolExecutor executor, String name) {
      this.name = name;
      this.group = new ThreadGroup(this.name);
   }

   public Thread newThread(Runnable r) {
      Thread t = new Thread(this.group, r);
      t.setName(this.name + "-" + this.threadNumber.getAndIncrement());
      t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
      return t;
   }

   public ThreadGroup getThreadGroup() {
      return this.group;
   }
}

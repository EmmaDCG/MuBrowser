package com.mu.utils.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ThreadCachedPoolManager {
   DB_SHORT("dbshort-thread", 1, 100, 30L, new LinkedBlockingQueue(), 3);

   private static Logger logger = LoggerFactory.getLogger(ThreadCachedPoolManager.class);
   private String name;
   private int minCount;
   private int maxCount;
   private long keepTime;
   private ThreadPoolExecutor executor;
   private ThreadPoolExecutor singleExecutor;
   private ThreadFactoryImpl factory;

   private ThreadCachedPoolManager(String name, int minCount, int maxCount, long keepTime, BlockingQueue queue, int rejectedCount) {
      this.name = name;
      this.minCount = minCount;
      this.maxCount = maxCount;
      this.keepTime = keepTime;
      this.executor = new ThreadPoolExecutor(minCount, maxCount, keepTime, TimeUnit.SECONDS, queue);
      this.factory = new ThreadFactoryImpl(this.executor, name);
      this.singleExecutor = new ThreadPoolExecutor(rejectedCount, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
      this.singleExecutor.setThreadFactory(new ThreadFactoryImpl(this.singleExecutor, name + "-RejectedHandler"));
      this.executor.setThreadFactory(this.factory);
      this.executor.setRejectedExecutionHandler(new CachedRejectedExecutionHandler(this.singleExecutor, name));
   }

   public String getName() {
      return this.name;
   }

   public int getMinCount() {
      return this.minCount;
   }

   public void setMinCount(int minCount) {
      this.minCount = minCount;
      this.executor.setCorePoolSize(minCount);
   }

   public int getMaxCount() {
      return this.maxCount;
   }

   public void setMaxCount(int maxCount) {
      this.maxCount = maxCount;
      this.executor.setMaximumPoolSize(maxCount);
   }

   public long getKeepTime() {
      return this.keepTime;
   }

   public void setKeepTime(long keepTime) {
      this.keepTime = keepTime;
      this.executor.setKeepAliveTime(keepTime, TimeUnit.SECONDS);
   }

   public void execute(Runnable command) {
      ThreadGroup tg = Thread.currentThread().getThreadGroup();
      if (tg != this.factory.getThreadGroup()) {
         Runnable command2 = new RunWrapper(command, true);
         this.executor.execute(command2);
      } else {
         logger.debug("use [{}] pool repeat [{}]", this.getName(), command);
         command.run();
      }

   }

   public ThreadPoolExecutor getExecutor() {
      return this.executor;
   }
}

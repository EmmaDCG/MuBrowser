package com.mu.utils.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServerRejectedExecutionHandler implements RejectedExecutionHandler {
   private static Logger logger = LoggerFactory.getLogger(ServerRejectedExecutionHandler.class);

   public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      if (!executor.isShutdown()) {
         logger.warn(r + " from " + executor, new RejectedExecutionException());
         if (Thread.currentThread().getPriority() > 5) {
            (new Thread(r)).start();
         } else {
            r.run();
         }

      }
   }
}

package com.mu.utils.concurrent;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedRejectedExecutionHandler implements RejectedExecutionHandler {
   private static Logger logger = LoggerFactory.getLogger(CachedRejectedExecutionHandler.class);
   private String name;
   private ThreadPoolExecutor handlerExecutor;

   public CachedRejectedExecutionHandler(ThreadPoolExecutor handlerExecutor, String name) {
      this.handlerExecutor = handlerExecutor;
      this.name = name;
   }

   public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      if (this.handlerExecutor != null && !this.handlerExecutor.isShutdown()) {
         if (r instanceof RunWrapper) {
            RunWrapper rw = (RunWrapper)r;
            logger.debug("rejected by [{}], queue size is {}, " + rw.getCallInfo(), this.name, this.handlerExecutor.getQueue().size());
         } else {
            logger.debug("rejected by [{}]", this.name);
         }

         this.handlerExecutor.execute(r);
      }
   }
}

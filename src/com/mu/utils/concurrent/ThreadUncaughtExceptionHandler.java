package com.mu.utils.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import org.apache.log4j.Logger;

public class ThreadUncaughtExceptionHandler implements UncaughtExceptionHandler {
   private static final Logger log = Logger.getLogger(ThreadUncaughtExceptionHandler.class);

   public void uncaughtException(Thread t, Throwable e) {
      log.error("Critical Error - Thread: " + t.getName() + " terminated abnormaly: " + e, e);
      if (e instanceof OutOfMemoryError) {
         log.error("Out of memory! You should get more memory!");
      }

   }
}

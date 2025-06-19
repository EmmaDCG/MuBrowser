package com.mu.utils.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunWrapper implements Runnable {
   private static Logger logger = LoggerFactory.getLogger(RunWrapper.class);
   private Runnable runnable;
   private boolean printInfo;
   private String callInfo = "can not get call info";

   public RunWrapper(Runnable runnable, boolean printInfo) {
      this.runnable = runnable;
      this.printInfo = printInfo;
      StackTraceElement[] stack = (new Throwable()).getStackTrace();
      if (stack.length >= 2 && stack[2] != null) {
         this.callInfo = "call at " + stack[2].getClassName() + "." + stack[2].getMethodName() + "():" + stack[2].getLineNumber();
      }

      stack = null;
   }

   public final void run() {
      try {
         long cur = System.currentTimeMillis();
         this.runnable.run();
         if (this.printInfo) {
            long useTime = System.currentTimeMillis() - cur;
            if (useTime > 300L) {
               logger.info(this.callInfo + " use time {}", useTime);
            }
         }
      } catch (Throwable var5) {
         logger.error(this.callInfo + " has a ", var5.toString());
         var5.printStackTrace();
      }

   }

   public String getCallInfo() {
      return this.callInfo;
   }
}

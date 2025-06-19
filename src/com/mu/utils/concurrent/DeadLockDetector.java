package com.mu.utils.concurrent;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import org.apache.log4j.Logger;

public class DeadLockDetector implements Runnable {
   private static final Logger log = Logger.getLogger(DeadLockDetector.class);
   private int checkInterval = 0;
   private static String INDENT = "    ";
   private StringBuilder sb = null;

   public DeadLockDetector(int checkInterval) {
      this.checkInterval = checkInterval * 1000;
   }

   public void run() {
      boolean noDeadLocks = true;

      while(noDeadLocks) {
         try {
            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            long[] threadIds = bean.findDeadlockedThreads();
            if (threadIds != null) {
               System.err.println("Deadlock detected!");
               this.sb = new StringBuilder();
               noDeadLocks = false;
               ThreadInfo[] infos = bean.getThreadInfo(threadIds);
               this.sb.append("\nTHREAD LOCK INFO: \n");
               ThreadInfo[] var8 = infos;
               int var7 = infos.length;

               ThreadInfo ti;
               int var6;
               for(var6 = 0; var6 < var7; ++var6) {
                  ti = var8[var6];
                  this.printThreadInfo(ti);
                  LockInfo[] lockInfos = ti.getLockedSynchronizers();
                  MonitorInfo[] monitorInfos = ti.getLockedMonitors();
                  this.printLockInfo(lockInfos);
                  this.printMonitorInfo(ti, monitorInfos);
               }

               this.sb.append("\nTHREAD DUMPS: \n");
               var7 = (var8 = bean.dumpAllThreads(true, true)).length;

               for(var6 = 0; var6 < var7; ++var6) {
                  ti = var8[var6];
                  this.printThreadInfo(ti);
               }

               log.error(this.sb.toString());
            }

            Thread.sleep((long)this.checkInterval);
         } catch (Exception var11) {
            var11.printStackTrace();
         }
      }

   }

   private void printThreadInfo(ThreadInfo threadInfo) {
      this.printThread(threadInfo);
      this.sb.append(INDENT + threadInfo.toString() + "\n");
      StackTraceElement[] stacktrace = threadInfo.getStackTrace();
      MonitorInfo[] monitors = threadInfo.getLockedMonitors();

      for(int i = 0; i < stacktrace.length; ++i) {
         StackTraceElement ste = stacktrace[i];
         this.sb.append(INDENT + "at " + ste.toString() + "\n");
         MonitorInfo[] var9 = monitors;
         int var8 = monitors.length;

         for(int var7 = 0; var7 < var8; ++var7) {
            MonitorInfo mi = var9[var7];
            if (mi.getLockedStackDepth() == i) {
               this.sb.append(INDENT + "  - locked " + mi + "\n");
            }
         }
      }

   }

   private void printThread(ThreadInfo ti) {
      this.sb.append("\nPrintThread\n");
      this.sb.append("\"" + ti.getThreadName() + "\"" + " Id=" + ti.getThreadId() + " in " + ti.getThreadState() + "\n");
      if (ti.getLockName() != null) {
         this.sb.append(" on lock=" + ti.getLockName() + "\n");
      }

      if (ti.isSuspended()) {
         this.sb.append(" (suspended)\n");
      }

      if (ti.isInNative()) {
         this.sb.append(" (running in native)\n");
      }

      if (ti.getLockOwnerName() != null) {
         this.sb.append(INDENT + " owned by " + ti.getLockOwnerName() + " Id=" + ti.getLockOwnerId() + "\n");
      }

   }

   private void printMonitorInfo(ThreadInfo threadInfo, MonitorInfo[] monitorInfos) {
      this.sb.append(INDENT + "Locked monitors: count = " + monitorInfos.length + "\n");
      MonitorInfo[] var6 = monitorInfos;
      int var5 = monitorInfos.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         MonitorInfo monitorInfo = var6[var4];
         this.sb.append(INDENT + "  - " + monitorInfo + " locked at " + "\n");
         this.sb.append(INDENT + "      " + monitorInfo.getLockedStackDepth() + " " + monitorInfo.getLockedStackFrame() + "\n");
      }

   }

   private void printLockInfo(LockInfo[] lockInfos) {
      this.sb.append(INDENT + "Locked synchronizers: count = " + lockInfos.length + "\n");
      LockInfo[] var5 = lockInfos;
      int var4 = lockInfos.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         LockInfo lockInfo = var5[var3];
         this.sb.append(INDENT + "  - " + lockInfo + "\n");
      }

   }
}

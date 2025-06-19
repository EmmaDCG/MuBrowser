package com.mu.game.task.schedule;

import com.mu.config.Global;
import com.mu.game.task.schedule.log.GlobalRoleLogTask;
import com.mu.game.task.schedule.log.ItemLogTask;
import com.mu.game.task.schedule.log.ReduceIngotLogTask;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;

public abstract class ScheduleTask implements Runnable {
   protected ScheduledFuture task;
   private static final ArrayList taskList = new ArrayList();
   protected boolean runWhenStop = false;

   public abstract void startTask();

   public abstract void doLocalTask();

   public abstract void doInterTask();

   public void stopTask() {
      if (this.runWhenStop) {
         this.run();
      }

      if (this.task != null) {
         this.task.cancel(true);
         this.task = null;
      }

   }

   public void run() {
      if (Global.isInterServiceServer()) {
         this.doInterTask();
      } else {
         this.doLocalTask();
      }

   }

   public static void start() {
      OnlinePlayerTask opt = new OnlinePlayerTask();
      opt.startTask();
      taskList.add(opt);
      MarketTask mt = new MarketTask();
      mt.startTask();
      taskList.add(mt);
      ShowItemTask sit = new ShowItemTask();
      sit.startTask();
      taskList.add(sit);
      OnlinePlayerEvilTask opet = new OnlinePlayerEvilTask();
      opet.startTask();
      taskList.add(opet);
      ExpiredItemTask eit = new ExpiredItemTask();
      eit.startTask();
      taskList.add(eit);
      ItemLogTask ilt = new ItemLogTask();
      ilt.startTask();
      taskList.add(ilt);
      ReduceIngotLogTask ril = new ReduceIngotLogTask();
      ril.startTask();
      taskList.add(ril);
      TopTask tt = new TopTask();
      tt.startTask();
      taskList.add(tt);
      GlobalRoleLogTask grt = new GlobalRoleLogTask();
      grt.startTask();
      taskList.add(grt);
      PayConfirmTask pct = new PayConfirmTask();
      pct.startTask();
      taskList.add(pct);
      SaveGangContributionTask st = new SaveGangContributionTask();
      st.startTask();
      taskList.add(st);
      CheckGangRedPacketTask ct = new CheckGangRedPacketTask();
      ct.startTask();
      taskList.add(ct);
      if (Global.isInterServiceServer()) {
         InterIdTask iit = new InterIdTask();
         iit.startTask();
         taskList.add(iit);
      }

      if (!Global.isDebug()) {
         HttpHeartbeat hb = new HttpHeartbeat();
         hb.startTask();
         taskList.add(hb);
      }

      Global.isInterServiceServer();
   }

   public static void stop() {
      Iterator it = taskList.iterator();

      while(it.hasNext()) {
         try {
            ScheduleTask st = (ScheduleTask)it.next();
            st.stopTask();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

      taskList.clear();
   }
}

package com.mu.game.task.specified.day;

import com.mu.game.task.specified.SpecifiedTimeManager;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DailyTask extends TimerTask {
   protected int hour = 0;
   protected int minute = 0;
   protected int second = 0;
   protected int days = 1;
   protected long lastExecuteTime = 0L;
   private static Logger logger = LoggerFactory.getLogger(DailyTask.class);

   public DailyTask(int hour, int minute, int second, int days) {
      this.hour = hour;
      this.minute = minute;
      this.second = second;
      this.days = days;
   }

   public DailyTask() {
   }

   public void start() throws Exception {
      if (this.days <= 0) {
         throw new Exception("increment error " + this.getClass().getName());
      } else {
         Calendar tc = Calendar.getInstance();
         tc.set(11, this.hour);
         tc.set(12, this.minute);
         tc.set(13, this.second);
         Date td = tc.getTime();
         Calendar nc = Calendar.getInstance();
         Date nd = nc.getTime();
         Date excuteDate;
         if (nd.before(td)) {
            excuteDate = td;
         } else {
            Date tmpDate;
            for(tmpDate = this.addDay(td); tmpDate.before(nd); tmpDate = this.addDay(tmpDate)) {
               ;
            }

            excuteDate = tmpDate;
         }

         SpecifiedTimeManager.schedule(this, excuteDate, 86400000L * (long)this.days);
      }
   }

   public final Date addDay(Date date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(6, this.days);
      return calendar.getTime();
   }

   public void run() {
      try {
         this.doTask();
         logger.info("do task this is {}", this.getClass().getName());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public abstract void doTask() throws Exception;
}

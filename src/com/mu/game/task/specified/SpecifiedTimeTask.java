package com.mu.game.task.specified;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SpecifiedTimeTask extends TimerTask {
   public static final int Day = 1;
   public static final int Hour = 2;
   public static final int Minute = 3;
   public static final int Second = 4;
   public static final int Week = 5;
   private int hour = 0;
   private int minute = 0;
   private int second = 0;
   private Date excuteDate = null;
   private int type = 1;
   private int increment = 1;
   private static Logger logger = LoggerFactory.getLogger(SpecifiedTimeTask.class);

   public SpecifiedTimeTask(int hour, int minute, int second, int type, int increment) {
      this.hour = hour;
      this.minute = minute;
      this.second = second;
      this.type = type;
      this.increment = increment;
      this.initExcuteDate();
   }

   public SpecifiedTimeTask(int type, int increment) {
      this.type = type;
      this.increment = increment;
   }

   public abstract SpecifiedTimeTask createNewTask();

   public abstract void start();

   public abstract void doTask();

   public void run() {
      try {
         this.doTask();
         logger.info("task run {}", this.getClass().getName());
      } catch (Exception var10) {
         var10.printStackTrace();
      } finally {
         try {
            SpecifiedTimeManager.schedule(this.createNewTask(), this.getExcuteDate());
         } catch (Exception var9) {
            var9.printStackTrace();
         }

      }

   }

   public Date getExcuteDate() {
      return this.excuteDate;
   }

   public Date getNextExcuteDate(Date date, int num) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      switch(this.type) {
      case 1:
         calendar.add(5, num);
         break;
      case 2:
         calendar.add(11, num);
         break;
      case 3:
         calendar.add(12, num);
         break;
      case 4:
         calendar.add(13, num);
      }

      return calendar.getTime();
   }

   public void initExcuteDate() {
      Calendar calendar = Calendar.getInstance();
      calendar.set(11, this.getHour());
      calendar.set(12, this.getMinute());
      calendar.set(13, this.getSecond());
      Date targetDate = calendar.getTime();
      Date curDate = new Date();
      if (curDate.before(targetDate)) {
         this.excuteDate = targetDate;
      } else {
         this.excuteDate = this.getNextExcuteDate(targetDate, this.increment);
      }

   }

   public void setExcuteDate(Date date) {
      this.excuteDate = date;
   }

   public final int getHour() {
      return this.hour;
   }

   public final int getMinute() {
      return this.minute;
   }

   public final int getSecond() {
      return this.second;
   }

   public final int getType() {
      return this.type;
   }

   public final int getIncrement() {
      return this.increment;
   }
}

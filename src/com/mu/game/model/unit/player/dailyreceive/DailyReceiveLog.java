package com.mu.game.model.unit.player.dailyreceive;

public class DailyReceiveLog {
   private int type;
   private int times;
   private long day;
   private int hour;

   public DailyReceiveLog(int type) {
      this.type = type;
   }

   public int getTimes() {
      return this.times;
   }

   public void setTimes(int times) {
      this.times = times;
   }

   public long getDay() {
      return this.day;
   }

   public void setDay(long day) {
      this.day = day;
   }

   public int getType() {
      return this.type;
   }

   public int getHour() {
      return this.hour;
   }

   public void setHour(int hour) {
      this.hour = hour;
   }
}

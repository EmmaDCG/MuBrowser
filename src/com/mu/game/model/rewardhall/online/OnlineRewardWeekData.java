package com.mu.game.model.rewardhall.online;

import com.mu.config.Global;
import java.util.Calendar;
import java.util.Date;

public class OnlineRewardWeekData {
   private int id;
   private int hourIngot;
   private int maxHour;
   private String str1;
   private String str2;
   private String str3;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getHourIngot() {
      return this.hourIngot;
   }

   public void setHourIngot(int hourIngot) {
      this.hourIngot = hourIngot;
   }

   public int getMaxHour() {
      return this.maxHour;
   }

   public void setMaxHour(int maxHour) {
      this.maxHour = maxHour;
   }

   public String getStr1() {
      return this.str1;
   }

   public void setStr1(String str1) {
      this.str1 = str1;
   }

   public String getStr2() {
      return this.str2;
   }

   public void setStr2(String str2) {
      this.str2 = str2;
   }

   public String getStr3() {
      return this.str3;
   }

   public void setStr3(String str3) {
      Date date = Global.getOpenServerTiem();
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      String s = "";
      switch(c.get(7)) {
      case 1:
         s = "日";
         break;
      case 2:
         s = "一";
         break;
      case 3:
         s = "二";
         break;
      case 4:
         s = "三";
         break;
      case 5:
         s = "四";
         break;
      case 6:
         s = "五";
         break;
      case 7:
         s = "六";
      }

      this.str3 = String.format(str3, s);
   }

   public int getAccumulative(int seconds) {
      return this.getHourIngot() * Math.min(this.getMaxHour(), seconds / 3600);
   }
}

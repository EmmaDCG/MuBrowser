package com.mu.utils;

import com.mu.config.MessageText;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {
   public static final long FullDayMillis = 86400000L;
   public static final long OneHourMillis = 3600000L;
   public static final long OneMinuteMillis = 60000L;
   public static final long OneWeekMillis = 604800000L;
   public static final long HalfHourMillis = 1800000L;

   public static long getTodayDateIncludeHour() {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(1);
      int month = cal.get(2) + 1;
      int day = cal.get(5);
      int hour = cal.get(11);
      return (long)(year * 1000000 + month * 10000 + day * 100 + hour);
   }

   public static long getTodayDateIncludeHour(int hour) {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(1);
      int month = cal.get(2) + 1;
      int day = cal.get(5);
      return (long)(year * 1000000 + month * 10000 + day * 100 + hour);
   }

   public static Date getDate(String str) {
      try {
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         return df.parse(str);
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Date getDate(String ts, String format) {
      try {
         DateFormat df = new SimpleDateFormat(format);
         return df.parse(ts);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static String getDateString(String ts, String format) {
      try {
         DateFormat df = new SimpleDateFormat(format);
         return df.format(df.parse(ts));
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static long getDayLong() {
      return getDayLong(Calendar.getInstance());
   }

   public static long getYesterdayLong() {
      Calendar calendar = Calendar.getInstance();
      calendar.add(5, -1);
      return getDayLong(calendar);
   }

   public static long getSomeDayLong(Calendar calendar, int day) {
      Calendar cal = Calendar.getInstance();
      cal.setTimeInMillis(calendar.getTimeInMillis());
      cal.add(5, day);
      return getDayLong(cal);
   }

   public static long getDayLong(Calendar cal) {
      int year = cal.get(1);
      int month = cal.get(2) + 1;
      int day = cal.get(5);
      return (long)(year * 10000 + month * 100 + day);
   }

   public static long getDayLong(String s) {
      try {
         Calendar cal = getCalendarDay(s);
         return getDayLong(cal);
      } catch (Exception var2) {
         var2.printStackTrace();
         return 0L;
      }
   }

   public static long getDayLong(Date date) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      return getDayLong(cal);
   }

   public static long getDayLong(long millis) {
      Calendar cal = Calendar.getInstance();
      cal.setTimeInMillis(millis);
      return getDayLong(cal);
   }

   public static Calendar getCalendarDay(String s) {
      try {
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date date = df.parse(s);
         Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         return cal;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static long strToMillis(String s) {
      try {
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date date = df.parse(s);
         return date.getTime();
      } catch (Exception var3) {
         var3.printStackTrace();
         return 0L;
      }
   }

   public static boolean timeBeforeHour(long time, int hourInDay) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(time);
      int h = calendar.get(11);
      return h < hourInDay;
   }

   public static String getTodayStr() {
      return getTimeStr(System.currentTimeMillis(), "yyyy-MM-dd");
   }

   public static String getTimeStr() {
      return getTimeStr(System.currentTimeMillis());
   }

   public static String getTimeStr(long time) {
      Date date = new Date(time);
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return format.format(date);
   }

   public static String getTimeStr(long time, String formatStr) {
      Date date = new Date(time);
      SimpleDateFormat format = new SimpleDateFormat(formatStr);
      return format.format(date);
   }

   public static boolean isSameDay(long date1, long date2) {
      return getTimeStr(date1, "yyyy-MM-dd").equals(getTimeStr(date2, "yyyy-MM-dd"));
   }

   public static long getSunday() {
      Calendar calendar = Calendar.getInstance();
      calendar.set(5, 1);
      int dayOfWeek = calendar.get(7);
      calendar.add(5, 1 - dayOfWeek);
      return calendar.getTimeInMillis();
   }

   public static int getDayOfMonth() {
      Calendar calendar = Calendar.getInstance();
      return calendar.get(5);
   }

   public static int getMonth() {
      Calendar calendar = Calendar.getInstance();
      return calendar.get(2);
   }

   public static long getTimeByDay(int day) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(5, day);
      return calendar.getTimeInMillis();
   }

   public static long getTimeStringToMills(String timeStr) {
      Date date = getDate(timeStr);
      if (date == null) {
         return 0L;
      } else {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         return calendar.getTimeInMillis();
      }
   }

   public static long getTimeStringToMills(String timeStr, String format) {
      Date date = getDate(timeStr, format);
      if (date == null) {
         return 0L;
      } else {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         return calendar.getTimeInMillis();
      }
   }

   public static String getOfflineDes(long offlineTime) {
      long tmp = System.currentTimeMillis() - offlineTime;
      if (tmp < 60000L) {
         return MessageText.getText(9017);
      } else {
         int weeks;
         if (tmp < 3600000L) {
            weeks = (int)(tmp / 60000L);
            return MessageText.getText(9018).replace("%s%", String.valueOf(weeks));
         } else if (tmp < 86400000L) {
            weeks = (int)(tmp / 3600000L);
            return MessageText.getText(9019).replace("%s%", String.valueOf(weeks));
         } else if (tmp < 604800000L) {
            weeks = (int)(tmp / 86400000L);
            return MessageText.getText(9020).replace("%s%", String.valueOf(weeks));
         } else {
            weeks = (int)(tmp / 604800000L);
            return MessageText.getText(9021).replace("%s%", String.valueOf(weeks));
         }
      }
   }

   public static long getTodayBegin() {
      Calendar calendar = Calendar.getInstance();
      calendar.set(11, 0);
      calendar.set(12, 0);
      calendar.set(13, 0);
      return calendar.getTimeInMillis();
   }

   public static long getTodayBegin(long millis) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(millis);
      calendar.set(11, 0);
      calendar.set(12, 0);
      calendar.set(13, 0);
      return calendar.getTimeInMillis();
   }
}

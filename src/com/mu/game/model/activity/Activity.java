package com.mu.game.model.activity;

import com.mu.game.model.unit.player.Player;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;

public abstract class Activity {
   protected int id;
   protected String name = "";
   protected Date openDate;
   protected Date closeDate;
   protected ArrayList elementList = new ArrayList();
   protected int sort = 0;
   protected int openType;
   protected int duration;
   protected String openTime;
   protected boolean systemClose = false;
   protected boolean isLoop = false;
   protected int digitalRelationId = 0;
   protected TimerTask openTask = null;
   protected TimerTask closeTask = null;

   public Activity(int id) {
      this.id = id;
      this.openTask = new TimerTask() {
         public void run() {
            Activity.this.open();
         }
      };
      this.closeTask = new TimerTask() {
         public void run() {
            Activity.this.close();
         }
      };
   }

   public void setSystemClose(boolean systemClose) {
      this.systemClose = systemClose;
   }

   public boolean isOpen() {
      if (!this.systemClose && this.openDate != null && this.closeDate != null) {
         Date curDate = Calendar.getInstance().getTime();
         return curDate.after(this.openDate) && curDate.before(this.closeDate);
      } else {
         return false;
      }
   }

   public TimerTask getOpenTask() {
      return this.openTask;
   }

   public TimerTask getCloseTask() {
      return this.closeTask;
   }

   public int getDigitalRelationId() {
      return this.digitalRelationId;
   }

   public long getOpenDateLong() {
      return this.openDate == null ? 0L : Time.getDayLong(this.openDate);
   }

   public long getCloseDateLong() {
      return this.closeDate == null ? 0L : Time.getDayLong(this.closeDate);
   }

   public abstract void init(Object var1) throws Exception;

   public abstract int getShellId();

   public void open() {
      this.allPlayerRefreshIcon();
   }

   public void close() {
      this.openDate = null;
      this.closeDate = null;
      this.allPlayerRefreshIcon();
   }

   public void allPlayerRefreshIcon() {
      ActivityManager.getShell(this.getShellId()).refreshIcon();
   }

   public void refreshIcon(Player player) {
      ActivityManager.getShell(this.getShellId()).refreshIcon(player);
   }

   public abstract int getActivityType();

   public abstract void writeDetail(Player var1);

   public void addElement(ActivityElement element, boolean toManager) {
      this.elementList.add(element);
      if (toManager) {
         ActivityManager.addEelemt(element);
      }

   }

   public ActivityElement getElement(int eid) {
      Iterator var3 = this.elementList.iterator();

      while(var3.hasNext()) {
         ActivityElement e = (ActivityElement)var3.next();
         if (e.getId() == eid) {
            return e;
         }
      }

      return null;
   }

   public ArrayList getElementList() {
      return this.elementList;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Date getOpenDate() {
      return this.openDate;
   }

   public void setOpenDate(Date openDate) {
      this.openDate = openDate;
   }

   public String getCloseTimeStr() {
      return this.closeDate == null ? "" : Time.getTimeStr(this.closeDate.getTime());
   }

   public void setCloseDate(Date closeDate) {
      this.closeDate = closeDate;
   }

   public int getSort() {
      return this.sort;
   }

   public void setSort(int sort) {
      this.sort = sort;
   }

   public int getId() {
      return this.id;
   }

   public Date getCloseDate() {
      return this.closeDate;
   }

   public int getOpenType() {
      return this.openType;
   }

   public void setOpenType(int openType) {
      this.openType = openType;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public String getOpenTime() {
      return this.openTime;
   }

   public void setOpenTime(String openTime) {
      this.openTime = openTime;
   }

   public boolean isLoop() {
      return this.isLoop;
   }

   public void setLoop(boolean isLoop) {
      this.isLoop = isLoop;
   }

   public int getCanReceiveNumber(Player player) {
      if (!this.isOpen()) {
         return 0;
      } else {
         int num = 0;
         Iterator var4 = this.getElementList().iterator();

         while(var4.hasNext()) {
            ActivityElement e = (ActivityElement)var4.next();
            if (e.canReceive(player, false)) {
               ++num;
            }
         }

         return num;
      }
   }

   public void destroy() {
      Iterator var2 = this.elementList.iterator();

      while(var2.hasNext()) {
         ActivityElement element = (ActivityElement)var2.next();
         ActivityManager.removeElement(element.getId());
         element.destroy();
      }

      try {
         if (this.openTask != null) {
            this.openTask.cancel();
            this.openTask = null;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      try {
         if (this.closeTask != null) {
            this.closeTask.cancel();
            this.closeTask = null;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      this.elementList.clear();
      this.elementList = null;
   }
}

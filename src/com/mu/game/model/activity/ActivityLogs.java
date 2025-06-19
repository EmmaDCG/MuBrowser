package com.mu.game.model.activity;

import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ActivityLogs {
   private ConcurrentHashMap roleLogs = new ConcurrentHashMap(8, 0.75F, 2);
   private ConcurrentHashMap userLogs = new ConcurrentHashMap(8, 0.75F, 2);
   private Player owner;

   public ActivityLogs(Player owner) {
      this.owner = owner;
   }

   public void addRoleLogs(int eid, Date date) {
      ArrayList list = (ArrayList)this.roleLogs.get(eid);
      if (list == null) {
         list = new ArrayList();
         this.roleLogs.put(eid, list);
      }

      list.add(date);
   }

   public void addUserLogs(int eid, Date date) {
      ArrayList list = (ArrayList)this.userLogs.get(eid);
      if (list == null) {
         list = new ArrayList();
         this.userLogs.put(eid, list);
      }

      list.add(date);
   }

   public int getReceiveTimesByRoleDaily(long day, int eid) {
      ArrayList list = (ArrayList)this.roleLogs.get(eid);
      if (list == null) {
         return 0;
      } else {
         int num = 0;
         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            Date date = (Date)var7.next();
            if (Time.getDayLong(date) == day) {
               ++num;
            }
         }

         return num;
      }
   }

   public int getReceiveTimesByUserDaily(long day, int eid) {
      ArrayList list = (ArrayList)this.userLogs.get(eid);
      if (list == null) {
         return 0;
      } else {
         int num = 0;
         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            Date date = (Date)var7.next();
            if (Time.getDayLong(date) == day) {
               ++num;
            }
         }

         return num;
      }
   }

   public int getReceiveTimesByRoleNotDaily(int eid, Date begin, Date end, boolean isLoop) {
      ArrayList list = (ArrayList)this.roleLogs.get(eid);
      if (list == null) {
         return 0;
      } else if (!isLoop) {
         return list.size();
      } else {
         int num = 0;
         Iterator var8 = list.iterator();

         while(var8.hasNext()) {
            Date date = (Date)var8.next();
            if (date.after(begin) && date.before(end)) {
               ++num;
            }
         }

         return num;
      }
   }

   public int getReceiveTimesByUserNotDaily(int eid, Date begin, Date end, boolean isLoop) {
      ArrayList list = (ArrayList)this.userLogs.get(eid);
      if (list == null) {
         return 0;
      } else if (!isLoop) {
         return list.size();
      } else {
         int num = 0;
         Iterator var8 = list.iterator();

         while(var8.hasNext()) {
            Date date = (Date)var8.next();
            if (date.after(begin) && date.before(end)) {
               ++num;
            }
         }

         return num;
      }
   }

   public void received(int receiveType, int sid, int eid, String date) {
      Date d = Time.getDate(date);
      WriteOnlyPacket packet = null;
      switch(receiveType) {
      case 1:
      case 2:
         this.addRoleLogs(eid, d);
         packet = Executor.SaveRoleActivityReceive.toPacket(this.owner.getID(), eid, date);
         break;
      case 3:
      case 4:
         this.addUserLogs(eid, d);
         packet = Executor.SaveUserActivityReceive.toPacket(this.owner.getUserName(), sid, eid, date);
      }

      if (packet != null) {
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      }

   }

   public void destroy() {
      this.roleLogs.clear();
      this.owner = null;
   }
}

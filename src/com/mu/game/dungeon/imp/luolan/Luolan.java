package com.mu.game.dungeon.imp.luolan;

import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangWarRankInfo;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.sys.CenterMessage;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Luolan extends Dungeon {
   private HashMap joinGang = new HashMap();
   private HashMap gangMark = new HashMap();
   private int prepareTime = 20;

   public Luolan(int id, LuolanTemplate t) {
      super(id, t);
      this.activeTime += this.prepareTime;
      this.keepNoCarePlayer = true;
      this.saveWhenInterrupt = false;
   }

   public void initMap() {
      LuolanMap map = new LuolanMap(((LuolanTemplate)this.getTemplate()).getDefaultMapID(), this);
      map.init();
      this.addMap(map);
      ArrayList list = GangManager.getGangWarRankInfoList(DungeonManager.getLuolanManager().getOpenDay());
      if (list == null || list.size() == 0) {
         GangManager.confirmRank();
         list = GangManager.getGangWarRankInfoList(DungeonManager.getLuolanManager().getOpenDay());
      }

      for(int i = 0; i < list.size(); ++i) {
         GangWarRankInfo info = (GangWarRankInfo)list.get(i);
         this.joinGang.put(info.getId(), i + 1);
         this.gangMark.put(info.getId(), new long[2]);
      }

      this.checkTimeFuture = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new Runnable() {
         public void run() {
            Luolan.this.checkTime();
         }
      }, 1000L, 1000L);
   }

   public HashMap getJoinGang() {
      return this.joinGang;
   }

   public boolean hasJoin(long gid) {
      return this.joinGang.containsKey(gid);
   }

   public int getTop(long gangId) {
      Integer in = (Integer)this.joinGang.get(gangId);
      return in == null ? -1 : in.intValue();
   }

   public int getGangTop(long gangId) {
      Integer in = (Integer)this.joinGang.get(gangId);
      return in == null ? -1 : in.intValue();
   }

   public Point getBorPoint(int top) {
      return (Point)((LuolanTemplate)this.getTemplate()).getBornMap().get(top);
   }

   public LuolanMap getLuolanMap() {
      return (LuolanMap)this.getFirstMap();
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }

   public void addMark(long gid) {
      if (!this.getLuolanMap().isEnd()) {
         long[] lo = (long[])this.gangMark.get(gid);
         if (lo != null) {
            ++lo[0];
            lo[1] = System.currentTimeMillis();
         }
      }
   }

   public HashMap getMarkMap() {
      return this.gangMark;
   }

   public void checkTime() {
      super.checkTime();
      if (this.getActiveTime() <= 0 && !this.isComplete()) {
         this.getLuolanMap().timeIsOver();
      }

      int tmp = this.prepareTime - this.getTimeCost();
      if (tmp > 0 && tmp % 60 == 0) {
         try {
            CenterMessage cm = new CenterMessage();
            cm.writeUTF(((LuolanTemplate)this.getTemplate()).getPrepareNotice().replace("%s%", String.valueOf(tmp / 60)));
            this.broadcastPacket(cm);
            cm.destroy();
            cm = null;
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      LuolanMap map = this.getLuolanMap();
      if (this.getTimeCost() >= this.prepareTime && map != null && !map.isBegin()) {
         try {
            CenterMessage cm = new CenterMessage();
            cm.writeUTF(((LuolanTemplate)this.getTemplate()).getStartStr());
            this.broadcastPacket(cm);
            cm.destroy();
            cm = null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         this.getLuolanMap().start();
      }

   }

   public int getStartTimeLeft() {
      int tmp = this.prepareTime - this.getTimeCost();
      if (tmp > 0) {
         return tmp % 60 == 0 ? tmp / 60 : tmp / 60 + 1;
      } else {
         return 0;
      }
   }

   public ArrayList getMarkList() {
      ArrayList list = new ArrayList();
      Iterator it = this.gangMark.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         long gid = ((Long)entry.getKey()).longValue();
         long[] lo = (long[])entry.getValue();
         Gang gang = GangManager.getGang(gid);
         if (gang != null) {
            MarkInfo mi = new MarkInfo();
            mi.setGangId(gid);
            mi.setTimes((int)lo[0]);
            mi.setMarkTime(lo[1]);
            list.add(mi);
         }
      }

      Collections.sort(list);
      return list;
   }

   public int getMarkTimes(long gid) {
      long[] lo = (long[])this.gangMark.get(gid);
      return lo == null ? 0 : (int)lo[0];
   }

   public synchronized void destroy() {
      try {
         super.destroy();
         DungeonManager.getLuolanManager().clearLuolan();
         this.joinGang.clear();
         this.gangMark.clear();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      DungeonManager.getLuolanManager().init();
   }
}

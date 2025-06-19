package com.mu.game.dungeon.imp.redfort;

import com.mu.game.CenterManager;
import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.item.Item;
import com.mu.game.model.team.Team;
import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.game.model.unit.player.popup.imp.DungeonQuitPopup;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.pkModel.ChangePkMode;
import com.mu.io.game.packet.imp.player.SelfExternal;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.CenterMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class RedFort extends Dungeon {
   public static final int MaxDieTimes = 3;
   private HashMap surviveMap = new HashMap();
   private ConcurrentHashMap killMap = new ConcurrentHashMap(8, 0.75F, 2);
   private ConcurrentHashMap redayQuitMap = new ConcurrentHashMap(8, 0.75F, 2);
   private ConcurrentHashMap dieMap = new ConcurrentHashMap(8, 0.75F, 2);
   private boolean isBegin = false;
   private int prepareTime = 180;
   private int currentFloor = 1;
   private int curFloorTime = 0;

   public RedFort(int id, RedFortTemplate t) {
      super(id, t);
      this.saveWhenInterrupt = false;
      this.prepareTime = t.getPrepareTime();
      this.keepNoCarePlayer = true;
      this.activeTime = t.getActiveTime();
      this.curFloorTime = this.prepareTime;
   }

   public void initMap() {
      ArrayList floorList = ((RedFortTemplate)this.getTemplate()).getFloorList();

      for(int i = 0; i < floorList.size(); ++i) {
         RedFortFloor floor = (RedFortFloor)floorList.get(i);
         int mapId = floor.getMapId();
         RedFortMap map = new RedFortMap(mapId, this, i + 1);
         this.addMap(map);
      }

   }

   public void pushResult(Player player) {
      RedFortKillRecord record = this.getRecord(player.getID());

      try {
         DungeonResult dr = new DungeonResult();
         dr.writeByte(((RedFortTemplate)this.getTemplate()).getTemplateID());
         dr.writeUTF(this.getResultTitle(record.getStatus()));
         dr.writeShort(record.getTop());
         dr.writeUTF(this.getResultContent(record.getStatus()));
         ArrayList list = ((RedFortTemplate)this.getTemplate()).getTopRewardItem(record.getTop());
         if (list == null) {
            dr.writeByte(0);
         } else {
            dr.writeByte(list.size());
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
               Item item = (Item)var6.next();
               GetItemStats.writeItem(item, dr);
            }
         }

         player.writePacket(dr);
         dr.destroy();
         dr = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   private String getResultTitle(int status) {
      switch(status) {
      case 1:
         return ((RedFortTemplate)this.getTemplate()).getSuccessTitle();
      case 2:
         return ((RedFortTemplate)this.getTemplate()).getDrawTitle();
      default:
         return ((RedFortTemplate)this.getTemplate()).getFaildTitle();
      }
   }

   private String getResultContent(int status) {
      switch(status) {
      case 1:
         return ((RedFortTemplate)this.getTemplate()).getSuccessContent();
      case 2:
         return ((RedFortTemplate)this.getTemplate()).getDrawContent();
      default:
         return ((RedFortTemplate)this.getTemplate()).getFialdContent();
      }
   }

   public void playerDie(long rid) {
      Integer in = (Integer)this.dieMap.get(rid);
      int value = in == null ? 1 : in.intValue() + 1;
      this.dieMap.put(rid, value);
   }

   public int getDieTimes(long rid) {
      Integer in = (Integer)this.dieMap.get(rid);
      return in == null ? 0 : in.intValue();
   }

   public RedFortMap getRedfFortMap(int floor) {
      DungeonMap map = this.getMapByIndex(floor - 1);
      return (RedFortMap)map;
   }

   public HashMap getSurviveMap() {
      return this.surviveMap;
   }

   public boolean hasKillRecord(long rid) {
      return this.killMap.containsKey(rid);
   }

   public void playerIn(Player player) {
      this.addPlayer(player);
      if (!this.hasKillRecord(player.getID())) {
         this.killMap.put(player.getID(), new RedFortKillRecord(player.getID(), player.getName()));
      }

      this.surviveMap.put(player.getID(), true);
      ChangePkMode.change(player, PkEnum.Mode_Peace.getModeID());
      if (player.getPetManager().isShow()) {
         player.getPetManager().hide();
      }

      SelfExternal.sendToClient(player, ((RedFortTemplate)this.getTemplate()).getExternalEntryList(player.getProType()));
      Team team = player.getCurrentTeam();
      if (team != null) {
         TeamManager.doOperation(5, player);
      }

   }

   public void addRedayQuitFuture(ScheduledFuture future, long rid) {
      if (!this.redayQuitMap.containsKey(rid)) {
         this.redayQuitMap.put(rid, future);
      }
   }

   public void exitForSwitchMap(Player player) {
      this.removeSurvivePlayer(player.getID());
      super.exitForSwitchMap(player);
   }

   public void removeSurvivePlayer(long rid) {
      this.surviveMap.remove(rid);
      int surviveNumber = this.surviveMap.size();
      if (surviveNumber <= 1 && this.isBegin) {
         this.doEnd();
      }

   }

   public void exitForInitiative(Player player, boolean force) {
      if (!force) {
         boolean showPop = true;
         RedFortKillRecord record = this.getRecord(player.getID());
         if (!record.isHasReceived()) {
            if (this.isComplete() && record.getTop() < 1) {
               showPop = false;
            }

            if (showPop) {
               DungeonQuitPopup pop = new DungeonQuitPopup(player.createPopupID());
               pop.setTitle(((RedFortTemplate)this.getTemplate()).getQuitTitle());
               pop.setContent(((RedFortTemplate)this.getTemplate()).getQuitContent());
               ShowPopup.open(player, pop);
               return;
            }
         }
      }

      this.removeSurvivePlayer(player.getID());
      super.exitForInitiative(player, force);
   }

   public void exitForIntterupt(Player player) {
      this.removeSurvivePlayer(player.getID());
      super.exitForIntterupt(player);
   }

   public int getSurviveSize() {
      return this.surviveMap.size();
   }

   public RedFortKillRecord getRecord(long rid) {
      return (RedFortKillRecord)this.killMap.get(rid);
   }

   public void doEnd() {
      if (!this.isComplete()) {
         this.setComplete(true);
         Iterator it = this.getMapList().iterator();

         DungeonMap map;
         while(it.hasNext()) {
            map = (DungeonMap)it.next();
            ((RedFortMap)map).stopCheck();
         }

         ArrayList list = new ArrayList();
         it = this.surviveMap.keySet().iterator();

         Player player;
         while(it.hasNext()) {
            player = CenterManager.getPlayerByRoleID(((Long)it.next()).longValue());
            if (player != null) {
               list.add(player);
            }
         }

         int size = list.size();
         if (size == 1) {
            player = (Player)list.get(0);
            RedFortKillRecord record = this.getRecord(player.getID());
            record.setTop(1);
            record.setStatus(1);
            this.pushResult(player);
         } else if (size > 1) {
            Iterator var10 = list.iterator();

            while(var10.hasNext()) {
               player = (Player)var10.next();
               RedFortKillRecord record = this.getRecord(player.getID());
               record.setTop(size);
               record.setStatus(2);
               this.pushResult(player);
            }
         }

         list.clear();
         map = null;

         try {
            CenterMessage cm = new CenterMessage();
            cm.writeUTF(((RedFortTemplate)this.getTemplate()).getEndStr());
            this.broadcastPacket(cm);
            cm.destroy();
            player = null;
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         Iterator it2 = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player player2 = (Player)it2.next();
            if (player2.getLevel() >= ((RedFortTemplate)this.getTemplate()).getLevelLimit()) {
               UpdateMenu.updateDungeonMenu(player2, 5);
            }
         }

      }
   }

   public Player removePlayer(Player player) {
      ScheduledFuture future = (ScheduledFuture)this.redayQuitMap.remove(player.getID());
      if (future != null) {
         future.cancel(true);
      }

      ChangePkMode.change(player, PkEnum.Mode_Peace.getModeID());
      if (!player.getPetManager().isShow()) {
         player.getPetManager().show();
      }

      ArrayList list = player.getCurrentExternal();
      SelfExternal.sendToClient(player, list);
      list.clear();
      list = null;
      return super.removePlayer(player);
   }

   public void setCurrentFloor(int f) {
      this.currentFloor = f;
   }

   public void checkTime() {
      super.checkTime();
      if (this.curFloorTime > 0) {
         --this.curFloorTime;
      }

      if (this.getActiveTime() <= 0 && !this.isComplete()) {
         this.doEnd();
      }

      int tmp = this.prepareTime - this.getTimeCost();
      CenterMessage cm;
      if (tmp > 0 && tmp % 60 == 0 && this.currentFloor == 1) {
         try {
            cm = new CenterMessage();
            cm.writeUTF(((RedFortTemplate)this.getTemplate()).getPrepareStart().replace("%s%", String.valueOf(tmp / 60)));
            this.broadcastPacket(cm);
            cm.destroy();
            cm = null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      if (this.getTimeCost() >= this.prepareTime && !this.isBegin) {
         try {
            cm = new CenterMessage();
            cm.writeUTF(((RedFortTemplate)this.getTemplate()).getStartStr());
            this.broadcastPacket(cm);
            cm.destroy();
            cm = null;
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         this.start();
      }

      if (!this.isComplete()) {
         this.pushSchedule();
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

   public void pushSchedule() {
      try {
         RedFortTemplate template = (RedFortTemplate)this.getTemplate();
         DungeonInfoUpdate du = new DungeonInfoUpdate();
         du.writeByte(template.getTemplateID());
         du.writeByte(this.currentFloor);
         du.writeByte(template.getMaxFloor());
         du.writeInt(this.curFloorTime);
         du.writeShort(this.getSurviveMap().size());
         du.writeUTF(((RedFortTemplate)this.getTemplate()).getPanelStr());
         this.broadcastPacket(du);
         du.destroy();
         du = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public boolean isBegin() {
      return this.isBegin;
   }

   public void start() {
      this.isBegin = true;
      RedFortMap map = this.getRedfFortMap(1);
      map.start();
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         ChangePkMode.change((Player)it.next(), PkEnum.Mode_Massacre.getModeID());
      }

      it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         if (player.getLevel() >= ((RedFortTemplate)this.getTemplate()).getLevelLimit()) {
            UpdateMenu.updateDungeonMenu(player, 5);
         }
      }

   }

   public void setCurFloorTime(int curFloorTime) {
      this.curFloorTime = curFloorTime;
   }

   public void setSurviveMap(HashMap surviveMap) {
      this.surviveMap = surviveMap;
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }

   public ArrayList getKillRecordList() {
      ArrayList list = new ArrayList();
      Iterator it = this.killMap.values().iterator();

      while(it.hasNext()) {
         list.add((RedFortKillRecord)it.next());
      }

      Collections.sort(list);
      return list;
   }

   public synchronized void destroy() {
      Iterator it = this.redayQuitMap.values().iterator();

      while(it.hasNext()) {
         ScheduledFuture future = (ScheduledFuture)it.next();
         future.cancel(true);
      }

      this.redayQuitMap.clear();
      this.surviveMap.clear();
      this.killMap.clear();
      this.dieMap.clear();
      ((RedFortTemplate)this.getTemplate()).getRedFortManager().clearRedFort();
      super.destroy();
   }
}

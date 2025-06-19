package com.mu.game.dungeon;

import com.mu.config.Global;
import com.mu.db.log.IngotChangeType;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.unit.buff.BuffManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.dungeon.QuitDungeon;
import com.mu.io.game.packet.imp.player.PlayerRevival;
import com.mu.utils.Rnd;
import com.mu.utils.Time;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Dungeon {
   private static Logger logger = LoggerFactory.getLogger(Dungeon.class);
   private DungeonTemplate template;
   private int id;
   private ArrayList mapList = new ArrayList();
   private ConcurrentHashMap playerMap = new ConcurrentHashMap(8, 0.75F, 2);
   private ConcurrentHashMap offlineBuffMap = new ConcurrentHashMap(8, 0.75F, 2);
   private ConcurrentHashMap playerInfoMap = new ConcurrentHashMap(8, 0.75F, 2);
   protected int timeLeft = 1800;
   protected int timeLimit = 0;
   protected int timeCost = 0;
   protected int activeTime = 0;
   protected ScheduledFuture uniqueFuture = null;
   protected ScheduledFuture checkTimeFuture = null;
   private boolean isSuccess = false;
   private boolean isComplete = false;
   protected long lastDorpTime = -1L;
   protected boolean isDestroy = false;
   protected boolean saveWhenInterrupt = true;
   private boolean playerInDungeon = false;
   protected int quitDelay = 120;
   private boolean switchMapMustQuit = true;
   protected boolean keepNoCarePlayer = false;
   private int curRankTimeCost = 0;

   public Dungeon(int id, DungeonTemplate t) {
      this.id = id;
      this.template = t;
      this.activeTime = this.timeLimit = this.getTemplate().getTimeLimit();
      this.timeLeft = this.timeLimit + this.quitDelay;
   }

   public void setActiveTime(int time) {
      this.activeTime = time;
   }

   public boolean isPlayerInDungeon() {
      return this.playerInDungeon;
   }

   public DungeonPlayerInfo removePlayerInfo(long rid) {
      return (DungeonPlayerInfo)this.playerInfoMap.remove(rid);
   }

   public void setLeftTime(int time) {
      this.timeLeft = time;
   }

   public void init() {
      this.initMap();
      this.checkTimeFuture = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new Runnable() {
         public void run() {
            Dungeon.this.checkTime();
         }
      }, 1000L, 1000L);
   }

   public int getTimeLimit() {
      return this.timeLimit;
   }

   public void addOfflineBuff(long rid, int buffId, int buffLevel, long duration) {
      this.offlineBuffMap.put(rid, new long[]{(long)buffId, (long)buffLevel, duration});
   }

   public DungeonTemplate getTemplate() {
      return this.template;
   }

   public int getQuitDelay() {
      return this.quitDelay;
   }

   public boolean mapInSelfDunegon(Map map) {
      for(int i = 0; i < this.mapList.size(); ++i) {
         DungeonMap m = (DungeonMap)this.mapList.get(i);
         if (m.equals(map)) {
            return true;
         }
      }

      return false;
   }

   public ArrayList getMapList() {
      return this.mapList;
   }

   public void readyToQuitForComplete() {
      this.setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            Dungeon.this.destroy();
         }
      }, (long)(this.quitDelay * 1000)));
   }

   public int getTimeLeft() {
      return this.timeLeft;
   }

   public void addMap(DungeonMap map) {
      this.mapList.add(map);
   }

   public void addPlayerInfo(DungeonPlayerInfo info) {
      this.playerInfoMap.put(info.getRid(), info);
   }

   public DungeonPlayerInfo getDungeonPlayerInfo(long rid) {
      return (DungeonPlayerInfo)this.playerInfoMap.get(rid);
   }

   public ConcurrentHashMap getPlayerInfoMap() {
      return this.playerInfoMap;
   }

   public abstract void initMap();

   public abstract DungeonResult createSuccessPacket();

   public int getID() {
      return this.id;
   }

   public void setCurRankTimeCost(int cost) {
      this.curRankTimeCost = cost;
   }

   public int getCurRankTimeCost() {
      return this.curRankTimeCost;
   }

   public void checkTime() {
      if (this.timeLeft <= 0) {
         this.destroy();
      } else {
         --this.timeLeft;
         ++this.timeCost;
         --this.activeTime;
         ++this.curRankTimeCost;
      }
   }

   public int getTimeCost() {
      return this.timeCost;
   }

   public int getActiveTime() {
      return this.activeTime;
   }

   public synchronized void cancelUniqueFuture() {
      if (this.uniqueFuture != null) {
         this.uniqueFuture.cancel(true);
         this.uniqueFuture = null;
      }

   }

   public synchronized void setUniqueFuture(ScheduledFuture future) {
      this.cancelUniqueFuture();
      this.uniqueFuture = future;
   }

   public void addPlayer(Player player) {
      this.playerMap.put(player.getID(), player);
      this.playerInDungeon = true;
      DungeonPlayerInfo info = (DungeonPlayerInfo)this.playerInfoMap.get(player.getID());
      if (info == null) {
         info = new DungeonPlayerInfo();
         info.setRid(player.getID());
         this.addPlayerInfo(info);
      }

   }

   public void createBuffWhenEnter(Player player) {
      DungeonPlayerInfo info = (DungeonPlayerInfo)this.playerInfoMap.get(player.getID());
      if (info != null) {
         if (info.getHurtInspireLevel() > 0) {
            player.getBuffManager().createAndStartBuff(player, 80007, info.getHurtInspireLevel(), true, 0L, (List)null);
         }

         if (info.getLifeInspireLevel() > 0) {
            player.getBuffManager().createAndStartBuff(player, 80006, info.getLifeInspireLevel(), true, 0L, (List)null);
         }

      }
   }

   public Player removePlayer(Player player) {
      try {
         this.removeInspireBuff(player);
         long[] offlineBuff = (long[])this.offlineBuffMap.remove(player.getID());
         if (offlineBuff != null) {
            BuffManager bm = player.getBuffManager();
            if (bm != null) {
               bm.createAndStartBuff(player, (int)offlineBuff[0], (int)offlineBuff[1], true, offlineBuff[2]);
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return (Player)this.playerMap.remove(player.getID());
   }

   public boolean hasPlayer(long rid) {
      return this.playerMap.containsKey(rid);
   }

   public ConcurrentHashMap getPlayerMap() {
      return this.playerMap;
   }

   public int checkMoneyInspire(Player player) {
      if (player.getMoney() < this.getTemplate().getInspireMoney()) {
         return 14019;
      } else {
         PlayerManager.reduceMoney(player, this.getTemplate().getInspireMoney());
         return 1;
      }
   }

   public int getMoneyInspireFullMsg() {
      return 14014;
   }

   public boolean ifMoneyInspireSuccess(Player player) {
      return Rnd.get(100) <= 45;
   }

   public int inspire(Player player, int type) {
      if (!this.getTemplate().isCanInspire()) {
         return 14013;
      } else {
         DungeonPlayerInfo info = this.getDungeonPlayerInfo(player.getID());
         boolean success = this.ifMoneyInspireSuccess(player);
         int iType = Rnd.get(2);
         if (info.getLifeInspireLevel() + info.getHurtInspireLevel() >= 20) {
            return 14015;
         } else if (type == 1) {
            if (info.getLifeInspireLevel() >= 5 && info.getHurtInspireLevel() >= 5) {
               return this.getMoneyInspireFullMsg();
            } else {
               int cm = this.checkMoneyInspire(player);
               if (cm != 1) {
                  return cm;
               } else if (!success) {
                  return 14016;
               } else {
                  if (iType == 0) {
                     if (info.getLifeInspireLevel() >= 5) {
                        iType = 1;
                     }
                  } else if (info.getHurtInspireLevel() >= 5) {
                     iType = 0;
                  }

                  this.doInspire(player, info, iType);
                  return 1;
               }
            }
         } else if (player.getIngot() < this.getTemplate().getInspireIngot()) {
            return 14020;
         } else {
            PlayerManager.reduceIngot(player, this.getTemplate().getInspireIngot(), this.getInspireIngotReduceType(), "");
            if (iType == 0) {
               if (info.getLifeInspireLevel() >= 10) {
                  iType = 1;
               }
            } else if (info.getHurtInspireLevel() >= 10) {
               iType = 0;
            }

            this.doInspire(player, info, iType);
            return 1;
         }
      }
   }

   private void doInspire(Player player, DungeonPlayerInfo info, int type) {
      if (type == 0) {
         info.setLifeInspireLevel(info.getLifeInspireLevel() + 1);
         player.getBuffManager().createAndStartBuff(player, 80006, info.getLifeInspireLevel(), true, 0L, (List)null);
      } else {
         info.setHurtInspireLevel(info.getHurtInspireLevel() + 1);
         player.getBuffManager().createAndStartBuff(player, 80007, info.getHurtInspireLevel(), true, 0L, (List)null);
      }

   }

   public IngotChangeType getInspireIngotReduceType() {
      return IngotChangeType.DungeonInspire;
   }

   public void exitForInitiative(Player player, boolean force) {
      try {
         if (player.isDie()) {
            PlayerRevival.revival(player);
         }

         if (!player.switchMap(player.getWorldMapID(), player.getWorldMapPoint())) {
            MapData md = MapConfig.getMapData(10001);
            player.switchMap(10001, new Point(md.getDefaultX(), md.getDefaultY()));
         }

         this.removePlayer(player);
         QuitDungeon.quit(this.getTemplate().getTemplateID(), player);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void exitForSwitchMap(Player player) {
      if (player.isDie()) {
         PlayerRevival.revival(player);
      }

      this.removePlayer(player);
      QuitDungeon.quit(this.getTemplate().getTemplateID(), player);
   }

   public void doFail(Player player) {
      this.setComplete(true);
      DungeonResult.faild(player, this.getTemplate().getTemplateID());
   }

   public void doSuccess() {
      this.setSuccess(true);
      this.setComplete(true);
      DungeonResult ds = this.createSuccessPacket();
      if (ds != null) {
         this.broadcastPacket(ds);
         ds.destroy();
         ds = null;
      }

   }

   public void exitForIntterupt(Player player) {
      if (!this.isComplete() && this.saveWhenInterrupt && !Global.isInterServiceServer()) {
         PlayerInterruptInfo info = new PlayerInterruptInfo(player.getID(), this.id, player.getDungeonMap().getMapIndex());
         info.setX(player.getX());
         info.setY(player.getY());
         DungeonManager.addInterruptInfo(info);
         this.lastDorpTime = info.getInterruptTime();
      }

      this.removePlayer(player);
   }

   public void removeInspireBuff(Player player) {
      BuffManager manager = player.getBuffManager();
      if (manager != null) {
         manager.endBuff(80007, true);
         manager.endBuff(80006, true);
      }

   }

   public int getMapSize() {
      return this.mapList == null ? 0 : this.mapList.size();
   }

   public DungeonMap getMapByIndex(int index) {
      return (DungeonMap)this.mapList.get(index);
   }

   public DungeonMap getFirstMap() {
      return this.mapList != null && this.mapList.size() != 0 ? (DungeonMap)this.mapList.get(0) : null;
   }

   public synchronized void destroy() {
      if (!this.isDestroy) {
         this.isDestroy = true;
         DungeonManager.removeDungeon(this.id);
         this.cancelUniqueFuture();
         if (this.checkTimeFuture != null) {
            this.checkTimeFuture.cancel(true);
            this.checkTimeFuture = null;
         }

         Iterator it = this.playerMap.values().iterator();

         while(it.hasNext()) {
            try {
               Player player = (Player)it.next();
               if (player.getUnitType() != 9) {
                  this.exitForInitiative(player, true);
               }
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }

         Iterator var7 = this.mapList.iterator();

         while(var7.hasNext()) {
            DungeonMap map = (DungeonMap)var7.next();

            try {
               map.setShouldDestory(true);
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }

         this.mapList.clear();
         this.mapList = null;
         this.playerMap.clear();
         this.playerMap = null;
         if (this.playerInfoMap != null) {
            this.playerInfoMap.clear();
         }

         if (logger.isInfoEnabled()) {
            logger.info("dungeon {} destroy {}", this.getTemplate().getTemplateID(), Time.getTimeStr(System.currentTimeMillis()));
         }

      }
   }

   public boolean isComplete() {
      return this.isComplete;
   }

   public void setComplete(boolean isComplete) {
      this.isComplete = isComplete;
   }

   public void setSuccess(boolean isSuccess) {
      this.isSuccess = isSuccess;
   }

   public boolean isSuccess() {
      return this.isSuccess;
   }

   public boolean isSwitchMapMustQuit() {
      return this.switchMapMustQuit;
   }

   public boolean canDestroy() {
      if (!this.playerInDungeon) {
         return false;
      } else {
         if (this.playerMap.size() == 0) {
            if (this.keepNoCarePlayer) {
               return false;
            }

            if (this.lastDorpTime <= 0L) {
               if (logger.isDebugEnabled()) {
                  logger.debug("dungeon destroy for last Player droped,id = {}", this.getTemplate().getTemplateID());
               }

               return true;
            }

            if (System.currentTimeMillis() - this.lastDorpTime >= 300000L) {
               if (logger.isDebugEnabled()) {
                  logger.debug("dungeon destroy for last Player drop over 5 minutes");
               }

               return true;
            }
         }

         return false;
      }
   }

   public boolean isDestroy() {
      return this.isDestroy;
   }

   public int getPlayerTimeLeft(Player player) {
      return this.getActiveTime();
   }

   public void broadcastPacket(WriteOnlyPacket packet) {
      Iterator it = this.playerMap.values().iterator();

      while(it.hasNext()) {
         ((Player)it.next()).writePacket(packet);
      }

   }
}

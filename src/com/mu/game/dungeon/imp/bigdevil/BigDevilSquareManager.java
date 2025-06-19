package com.mu.game.dungeon.imp.bigdevil;

import com.mu.config.BroadcastManager;
import com.mu.game.CenterManager;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.game.top.BigDevilTopInfo;
import com.mu.game.top.DungeonTopManager;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.sys.ZMDMessage;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigDevilSquareManager {
   private ConcurrentHashMap squareMap = Tools.newConcurrentHashMap2();
   private ArrayList startTimeList = new ArrayList();
   private ArrayList taskList = new ArrayList();
   private int squareNumber = 0;
   private boolean isOpen = false;
   private boolean isBegin = false;
   private BigDevilSquareTemplate template;
   private ScheduledFuture future = null;
   private int timeCost = 0;
   private BigDevilTopInfo historyInfo = null;
   private long historyExp = -1L;
   private static Logger logger = LoggerFactory.getLogger(BigDevilSquareManager.class);

   public BigDevilSquareManager(BigDevilSquareTemplate template) {
      this.template = template;
   }

   public void addStartTime(int[] time) {
      this.startTimeList.add(time);
   }

   public Date getDate(int hour, int minute, int second) {
      Calendar tc = Calendar.getInstance();
      tc.set(11, hour);
      tc.set(12, minute);
      tc.set(13, second);
      return tc.getTime();
   }

   private void checkTime() {
      ++this.timeCost;
      if (this.timeCost >= this.template.getPrepareTime() && !this.isBegin()) {
         this.bigDevilStart();
      }

      if (this.timeCost >= this.template.getTimeLimit() + this.template.getPrepareTime() && this.isOpen()) {
         this.bigDevilClose();
      }

   }

   private void startCheck() {
      this.cancelCheck();
      this.future = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(new Runnable() {
         public void run() {
            BigDevilSquareManager.this.checkTime();
         }
      }, 1000L, 1000L);
   }

   private void cancelCheck() {
      if (this.future != null) {
         this.future.cancel(true);
         this.future = null;
      }

   }

   public void changeHistoryInfo() {
      this.historyInfo = DungeonTopManager.getBigDevilTopInfo(1);
      if (this.historyInfo != null) {
         this.historyExp = this.historyInfo.getExp();
      }

   }

   public void createBigDevil() {
      Iterator it = this.squareMap.values().iterator();

      while(it.hasNext()) {
         try {
            ((BigDevilSquare)it.next()).destroy();
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      this.squareMap.clear();
      this.setOpen(true);
      this.timeCost = 0;
      this.updateBigDevilMenu();
      this.startCheck();
      String text = this.template.getPrepareStartStr().replace("%s%", String.valueOf(this.template.getPrepareTime() / 60));
      ZMDMessage zm = new ZMDMessage(text);
      DunTimingPanel ap = this.template.getTimingPanel();
      it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         if (FunctionOpenManager.isOpen(player, 20)) {
            player.writePacket(ap);
            player.writePacket(zm);
         }
      }

      ap.destroy();
      ap = null;
      zm.destroy();
      zm = null;
      this.changeHistoryInfo();
      this.squareNumber = 0;
      logger.info("big devil open ....");
   }

   public synchronized BigDevilSquare getBigDevilSquare(Player player) {
      if (!this.isBegin() && this.isOpen()) {
         BigDevilSquareLevel bl = this.template.getPlayerFitLevel(player);
         if (bl == null) {
            return null;
         } else {
            BigDevilSquare square = null;
            Iterator it = this.squareMap.values().iterator();

            while(it.hasNext()) {
               BigDevilSquare bs = (BigDevilSquare)it.next();
               if (bs.getSquareLevel().getLevel() == bl.getLevel() && bs.getPlayerMap().size() < this.template.getMaxPlayer()) {
                  square = bs;
                  break;
               }
            }

            if (square == null) {
               square = new BigDevilSquare(DungeonManager.getID(), this.template, bl);
               int activeTime = this.template.getTimeLimit() + this.template.getPrepareTime() - this.timeCost;
               square.setActiveTime(activeTime);
               square.setLeftTime(activeTime + 30);
               square.init();
               this.squareMap.put(square.getID(), square);
               ++this.squareNumber;
            }

            return square;
         }
      } else {
         return null;
      }
   }

   private void bigDevilStart() {
      this.setBegin(true);
      this.updateBigDevilMenu();
      Iterator zm = this.squareMap.values().iterator();

      while(zm.hasNext()) {
         try {
            BigDevilSquare bs = (BigDevilSquare)zm.next();
            Iterator itPlayer = bs.getPlayerMap().values().iterator();

            while(itPlayer.hasNext()) {
               Player player = (Player)itPlayer.next();
               player.getDunLogsManager().finishDungeon(6, 0);
               player.getDunLogsManager().addTotalNumber(6, 0);
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      ZMDMessage zm2 = new ZMDMessage(this.template.getStartStr());
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         player.writePacket(zm2);
      }

      zm2.destroy();
      zm2 = null;
      logger.info("big devil start ....");
   }

   public void reduceSquareNumber() {
      --this.squareNumber;
      if (this.squareNumber == 0) {
         this.doBroadcast();
         DungeonTopManager.restBigdevilList();
      }

   }

   private void doBroadcast() {
      BigDevilTopInfo ti = DungeonTopManager.getBigDevilTopInfo(1);
      if (ti != null) {
         if (this.historyInfo != null && ti.getExp() <= this.historyExp) {
            BroadcastManager.broadcastBigDevilNoNewRecord(this.historyInfo, this.template.getNoNewRecord());
         } else {
            BroadcastManager.broadcastBigDevilNewRecord(ti, this.template.getNewRecord());
         }

      }
   }

   private void bigDevilClose() {
      logger.info("big devil close ....");
      this.setOpen(false);
      this.setBegin(false);
      this.cancelCheck();
      this.updateBigDevilMenu();
   }

   public void start() {
      Iterator it = this.squareMap.values().iterator();

      while(it.hasNext()) {
         ((BigDevilSquare)it.next()).destroy();
      }

      Iterator var2 = this.taskList.iterator();

      while(var2.hasNext()) {
         BigDevilTask task = (BigDevilTask)var2.next();
         task.cancel();
      }

      this.taskList.clear();
      SpecifiedTimeManager.purge();

      for(int i = 0; i < this.startTimeList.size(); ++i) {
         int[] startTime = (int[])this.startTimeList.get(i);
         Date startDate = this.getDate(startTime[0], startTime[1], startTime[2]);
         Calendar startCalendar = Calendar.getInstance();
         startCalendar.setTime(startDate);
         BigDevilTask rt = new BigDevilTask(startTime[0], startTime[1], startTime[2]);

         try {
            rt.start();
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

   }

   public void setBegin(boolean isBegin) {
      this.isBegin = isBegin;
   }

   public void setOpen(boolean b) {
      this.isOpen = b;
   }

   public boolean isOpen() {
      return this.isOpen;
   }

   public boolean isBegin() {
      return this.isBegin;
   }

   public void removeSquare(int id) {
      this.squareMap.remove(id);
   }

   private void updateBigDevilMenu() {
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         if (player.getLevel() >= this.template.getMinLevel()) {
            try {
               UpdateMenu.updateDungeonMenu(player, 6);
               this.template.writeDungeonInfo(player);
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      }

   }

   public int getNextOpenTimeLeft() {
      if (this.isOpen()) {
         return -1;
      } else {
         Date nowDate = Calendar.getInstance().getTime();
         long now = System.currentTimeMillis();
         long space = -1L;

         for(int i = 0; i < this.startTimeList.size(); ++i) {
            int[] startTime = (int[])this.startTimeList.get(i);
            Date startDate = this.getDate(startTime[0], startTime[1], startTime[2]);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            if (startDate.before(nowDate)) {
               startCalendar.add(6, 1);
            }

            long tmpSpace = startCalendar.getTimeInMillis() - now;
            if (tmpSpace > 0L && (space < 0L || tmpSpace < space)) {
               space = tmpSpace;
            }
         }

         if (space > 0L) {
            return (int)(space / 1000L);
         } else {
            return -1;
         }
      }
   }
}

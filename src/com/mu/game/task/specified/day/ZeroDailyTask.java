package com.mu.game.task.specified.day;

import com.mu.db.manager.GlobalLogDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.drop.SystemDropManager;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.item.operation.ItemManager;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.action.DelayAction;
import com.mu.game.model.unit.action.imp.NightPkProtectedAction;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.io.game.packet.imp.map.EnterMapSuccess;
import com.mu.io.game.packet.imp.monster.RefreshSingleBoss;
import com.mu.utils.Time;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.Calendar;
import java.util.Iterator;

public class ZeroDailyTask extends DailyTask {
   public static long lastClearTime = 0L;

   static {
      Calendar tc = Calendar.getInstance();
      tc.set(11, 0);
      tc.set(12, 0);
      tc.set(13, 0);
      lastClearTime = tc.getTimeInMillis();
   }

   public ZeroDailyTask() {
      super(0, 0, 0, 1);
   }

   public void clearTodayOnlineTime(Player plyaer) {
      plyaer.setTodayOnlineTime(0);
   }

   public void clearRedeemAnyItemAmount(Player player) {
      ItemManager manager = player.getItemManager();
      if (manager != null) {
         manager.clearLimitCount();
      }

   }

   public void clearFcm(Player player) {
      try {
         player.getUser().setOnlineTime(0);
         player.getUser().setRestTime(0);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void doZeroPkProtected(Player player, long interval) {
      try {
         player.getBuffManager().createAndStartBuff(player, 80008, 1, true, interval);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void dealNightProtectedAction(long interval) {
      final DelayAction da = new DelayAction(new NightPkProtectedAction(), 50L);
      EnterMapSuccess.addGlobalEnterMapAction(da);
      ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            EnterMapSuccess.removeGlobalEnterMapAction(da);
         }
      }, interval);
   }

   public static void checkNightProtectedWhenServerStar() {
      long todayBegin = Time.getTodayBegin();
      long nightProtectedInterval = (long)BuffModel.getModel(80008).getIntervalTime();
      long now = System.currentTimeMillis();
      long interval = todayBegin + nightProtectedInterval - now;
      if (interval > 10000L) {
         dealNightProtectedAction(interval);
      }

   }

   public static void purge() {
      ThreadFixedPoolManager.POOL_OTHER.purge();
      SpecifiedTimeManager.purge();
   }

   public static void gangDayChanged() {
      Iterator it = GangManager.getGangMap().values().iterator();

      while(it.hasNext()) {
         ((Gang)it.next()).dayChanged();
      }

   }

   public static void clearFriend(Player player) {
      FriendManager manager = player.getFriendManager();
      if (manager != null) {
         manager.dayChanged();
      }

   }

   public void doTask() throws Exception {
      lastClearTime = System.currentTimeMillis();
      SystemDropManager.dayClear();
      long nightProtectedInterval = (long)BuffModel.getModel(80008).getIntervalTime();
      dealNightProtectedAction(nightProtectedInterval);
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();

         try {
            player.getOffLineManager().dayChange();
            this.clearFcm(player);
            player.getOnlineManager().onEventSkipDay();
            this.clearTodayOnlineTime(player);
            this.doZeroPkProtected(player, nightProtectedInterval);
            player.getVitalityManager().onEventSkipDay();
            player.getVIPManager().onSkipDay();
            player.getTaskManager().onEventSkipDay();
            player.getSignManager().onEventSkipDay();
            player.getFinancingManager().onDaySkip();
            this.clearRedeemAnyItemAmount(player);
            player.getDunLogsManager().clearLogs();
            player.getRedPacketManager().dayChange();
            player.getSevenManager().onDaySkip();
            clearFriend(player);
            RefreshSingleBoss.refreshAllSingleBoss(player);
            DynamicMenuManager.refreshAllMenuWhenChangeDay(player);
            player.getTanXianManager().setIngotCount(0);
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      GlobalLogDBManager.saveLogInLogWhenZeroTime();
      gangDayChanged();
      purge();
   }
}

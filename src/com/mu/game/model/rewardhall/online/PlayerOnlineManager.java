package com.mu.game.model.rewardhall.online;

import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.Executor;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.rewardhall.online.OnlineRewardInform;
import com.mu.io.game.packet.imp.rewardhall.online.OnlineRewardInit;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerOnlineManager {
   private static Logger logger = LoggerFactory.getLogger(PlayerOnlineManager.class);
   private Player owner;
   private int yesterdayOnlineTime = 0;
   private CopyOnWriteArrayList dayReceiveList;
   private long weekReceiveTime;
   private int weekIndex;
   private int weekSeconds;
   private int weekAccumulative;
   int receiveNumber = 0;

   public PlayerOnlineManager(Player owner) {
      this.owner = owner;
      this.weekIndex = RewardHallConfigManager.getOnlineRewardWeekIndex();
      this.dayReceiveList = new CopyOnWriteArrayList();
   }

   public void init(OnlineRewardInit packet) {
      try {
         boolean has = packet.readBoolean();
         if (has) {
            String dayReceiveStr = packet.readUTF();
            long weekReceiveTime = packet.readLong();
            int weekIndex = packet.readInt();
            int weekSeconds = packet.readInt();
            int weekAccumulative = packet.readInt();
            if (!this.getOwner().isNeedZeroClear()) {
               Matcher m = CommonRegPattern.PATTERN_INT.matcher(dayReceiveStr);

               while(m.find()) {
                  int id = Integer.parseInt(m.group());
                  OnlineRewardDayData data = RewardHallConfigManager.getOnlineRewardData(id);
                  if (data == null) {
                     logger.error("role init online reward can not found OnlineRewardDayData by id {}", id);
                  } else {
                     this.dayReceiveList.add(id);
                  }
               }
            }

            OnlineRewardWeekData week = RewardHallConfigManager.getOnlineRewardWeek(weekIndex);
            this.weekReceiveTime = weekIndex != this.weekIndex ? 0L : weekReceiveTime;
            this.weekSeconds = weekIndex != this.weekIndex ? 0 : weekSeconds + this.yesterdayOnlineTime;
            this.weekAccumulative = weekIndex != this.weekIndex ? week.getAccumulative(weekSeconds) : weekAccumulative;
         }
      } catch (Exception var12) {
         logger.error("role init reward hall data error!");
         var12.printStackTrace();
      }

   }

   public Player getOwner() {
      return this.owner;
   }

   public int getWeekSeconds() {
      return this.weekSeconds + this.getOwner().getTodayOnlineTime() + 5;
   }

   public long getWeekReceiveTime() {
      return this.weekReceiveTime;
   }

   public void receiveDayReward() {
      if (RewardHallConfigManager.getOnlineRewardSize() > this.dayReceiveList.size()) {
         List list = new ArrayList();
         Iterator it = RewardHallConfigManager.getOnlineRewardIterator();

         while(it.hasNext()) {
            OnlineRewardDayData data = (OnlineRewardDayData)it.next();
            if (data.getSeconds() <= this.owner.getTodayOnlineTime() + 5 && !this.dayReceiveList.contains(data.getId())) {
               OperationResult r = this.getOwner().getItemManager().addItem(data.getIDU());
               if (!r.isOk()) {
                  SystemMessage.writeMessage(this.getOwner(), r.getResult());
                  break;
               }

               this.dayReceiveList.add(data.getId());
               list.add(data.getId());
            }
         }

         if (!list.isEmpty()) {
            OnlineRewardInform.sendReceiveDayResult(this.getOwner(), list);
            this.dbReplaceOnline();
            UpdateMenu.update(this.getOwner(), 11);
            ActivityChangeDigital.pushDigital(this.getOwner(), 2, this.getCanReceiveCount());
         }

         list.clear();
         list = null;
      }
   }

   public void onEventAddOnlineTime() {
      if (this.getOwner().isEnterMap()) {
         if (RewardHallConfigManager.getOnlineRewardSize() > this.dayReceiveList.size()) {
            Iterator it = RewardHallConfigManager.getOnlineRewardIterator();

            while(it.hasNext()) {
               OnlineRewardDayData data = (OnlineRewardDayData)it.next();
               if (data.getSeconds() <= this.owner.getTodayOnlineTime() && !this.dayReceiveList.contains(data.getId())) {
                  int number = this.getCanReceiveCount();
                  if (number != this.receiveNumber) {
                     this.receiveNumber = number;
                     UpdateMenu.update(this.getOwner(), 11);
                     ActivityChangeDigital.pushDigital(this.getOwner(), 2, number);
                  }
                  break;
               }
            }

         }
      }
   }

   public void receiveWeekReward() {
      if (this.weekReceiveTime == 0L && this.weekAccumulative != 0) {
         PlayerManager.addBindIngot(this.getOwner(), this.weekAccumulative, IngotChangeType.WeekReward.getType());
         this.weekReceiveTime = System.currentTimeMillis();
         this.dbReplaceOnline();
         SystemMessage.writeMessage(this.getOwner(), String.format(MessageText.getText(24201), this.weekAccumulative), 24201);
         UpdateMenu.update(this.getOwner(), 11);
         ActivityChangeDigital.pushDigital(this.getOwner(), 2, this.getCanReceiveCount());
      }
   }

   public void onEventSkipDay() {
      try {
         this.dayReceiveList.clear();
         int weekIndex = RewardHallConfigManager.getOnlineRewardWeekIndex();
         if (this.weekIndex != weekIndex) {
            this.weekAccumulative = RewardHallConfigManager.getOnlineRewardWeek(this.weekIndex).getAccumulative(this.weekSeconds);
            this.weekIndex = weekIndex;
            this.weekSeconds = 0;
            this.weekReceiveTime = 0L;
         } else {
            this.weekSeconds = this.getWeekSeconds();
         }

         this.dbReplaceOnline();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void dbReplaceOnline() {
      try {
         WriteOnlyPacket packet = Executor.ReplaceOnline.toPacket(this.getOwner().getID(), this.dayReceiveList.toString(), this.weekReceiveTime, this.weekIndex, this.weekSeconds, this.weekAccumulative);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public int getWeekAccumulative() {
      return this.weekAccumulative;
   }

   public CopyOnWriteArrayList getDayReceiveList() {
      return this.dayReceiveList;
   }

   public int getYesterdayOnlineTime() {
      return this.yesterdayOnlineTime;
   }

   public void setYesterdayOnlineTime(int yesterdayOnlineTime) {
      this.yesterdayOnlineTime = yesterdayOnlineTime;
   }

   public int getCanReceiveCount() {
      int canReceiveCount = 0;
      Iterator it = RewardHallConfigManager.getOnlineRewardIterator();

      while(it.hasNext()) {
         OnlineRewardDayData data = (OnlineRewardDayData)it.next();
         if (data.getSeconds() <= this.owner.getTodayOnlineTime() + 5 && !this.dayReceiveList.contains(data.getId())) {
            ++canReceiveCount;
         }
      }

      if (this.weekReceiveTime == 0L && this.weekAccumulative != 0) {
         ++canReceiveCount;
      }

      return canReceiveCount;
   }

   public void destroy() {
      this.owner = null;
      if (this.dayReceiveList != null) {
         this.dayReceiveList.clear();
         this.dayReceiveList = null;
      }

   }
}

package com.mu.game.model.rewardhall.sign;

import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.Executor;
import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.rewardhall.sign.SignInform;
import com.mu.io.game.packet.imp.rewardhall.sign.SignInit;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerSignManager {
   private static Logger logger = LoggerFactory.getLogger(PlayerSignManager.class);
   private Player owner;
   private int round = 1;
   private Calendar roundStart = Calendar.getInstance();
   private Calendar signTime = Calendar.getInstance();
   private Calendar[] timeSet = new Calendar[0];
   private CopyOnWriteArraySet rewardList = new CopyOnWriteArraySet();

   public PlayerSignManager(Player player) {
      this.owner = player;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void init(SignInit packet) {
      try {
         boolean has = packet.readBoolean();
         if (has) {
            this.round = packet.readInt();
            this.roundStart.setTimeInMillis(packet.readLong());
            this.signTime.setTimeInMillis(packet.readLong());
            List timeList = new ArrayList();
            int count = packet.readByte();
            long cur = System.currentTimeMillis();

            for(int i = 0; i < count; ++i) {
               long time = packet.readLong();
               if (time <= cur) {
                  Calendar tc = Calendar.getInstance();
                  timeList.add(tc);
                  tc.setTimeInMillis(time);
               }
            }

            this.timeSet = (Calendar[])timeList.toArray(this.timeSet);
            byte[] bytes = new byte[packet.readByte()];
            packet.readBytes(bytes);

            for(int i = 0; i < bytes.length; ++i) {
               SignRewardData data = RewardHallConfigManager.getSignRewardData(bytes[i]);
               if (data != null) {
                  this.rewardList.add(data);
               }
            }

            if (this.timeSet.length >= RewardHallConfigManager.getSignRoundDays() && !this.equal(this.signTime, Calendar.getInstance())) {
               ++this.round;
               this.roundStart.setTimeInMillis(cur);
               Calendar lc = this.getLastSign();
               if (lc != null) {
                  this.roundStart.setTimeInMillis(lc.getTimeInMillis());
                  this.roundStart.add(5, 1);
               }

               this.signTime.setTimeInMillis(cur);
               this.timeSet = new Calendar[0];
               this.rewardList.clear();
               this.dbReplaceSign();
            }
         } else {
            this.dbReplaceSign();
         }
      } catch (Exception var11) {
         logger.error("role init reward hall data error!");
         var11.printStackTrace();
      }

   }

   public void onLogin() {
   }

   public boolean canSign(long time) {
      Calendar tc = Calendar.getInstance();
      tc.setTimeInMillis(time);
      int year = tc.get(1);
      int day = tc.get(6);
      Calendar start = this.getRoundStart();
      int startYear = start.get(1);
      int startDay = start.get(6);
      Calendar mc = Calendar.getInstance();
      mc.set(8, 0);
      mc.set(7, 1);
      int mcYear = mc.get(1);
      int mcDay = mc.get(6);
      Calendar cur = Calendar.getInstance();
      int curYear = cur.get(1);
      int curDay = cur.get(6);
      if (year >= mcYear && (year != mcYear || day >= mcDay) && year >= startYear && (year != startYear || day >= startDay) && year <= curYear && (year != curYear || day <= curDay)) {
         Calendar[] timeSet = this.getTimeSet();

         for(int j = 0; j < timeSet.length; ++j) {
            if (timeSet[j].get(1) == year && timeSet[j].get(6) == day) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isSigned() {
      Calendar tc = Calendar.getInstance();
      int year = tc.get(1);
      int day = tc.get(6);
      Calendar[] timeSet = this.getTimeSet();

      for(int j = 0; j < timeSet.length; ++j) {
         if (timeSet[j].get(1) == year && timeSet[j].get(6) == day) {
            return true;
         }
      }

      return false;
   }

   public Calendar getLastSign() {
      Calendar c = null;
      Calendar[] timeSet = this.getTimeSet();

      for(int j = 0; j < timeSet.length; ++j) {
         if (c == null || timeSet[j].get(1) > c.get(1) || timeSet[j].get(1) == c.get(1) && timeSet[j].get(6) > c.get(6)) {
            c = timeSet[j];
         }
      }

      return c;
   }

   public int compare(Calendar c1, Calendar c2) {
      int year1 = c1.get(1);
      int day1 = c1.get(6);
      int year2 = c2.get(1);
      int day2 = c2.get(6);
      return year1 < year2 || year1 == year2 && day1 < day2 ? -1 : (year1 == year2 && day1 == day2 ? 0 : 1);
   }

   public boolean equal(Calendar c1, Calendar c2) {
      return c1.get(1) == c2.get(1) && c1.get(6) == c2.get(6);
   }

   public void signBefore(long time) {
      if (!this.owner.getVIPManager().getEffectBooleanValue(VIPEffectType.VE_20)) {
         SystemMessage.writeMessage(this.owner, MessageText.getText(23100), 23100);
      } else if (this.canSign(time)) {
         if (1 != PlayerManager.reduceIngot(this.getOwner(), 1, IngotChangeType.SIGN, "")) {
            SystemMessage.writeMessage(this.getOwner(), 1015);
         } else {
            this.sign(time);
         }
      }
   }

   public void signToday() {
      if (!this.isSigned()) {
         this.sign(System.currentTimeMillis());
      }
   }

   private void sign(long time) {
      Calendar[] timeSet = new Calendar[this.timeSet.length + 1];
      System.arraycopy(this.timeSet, 0, timeSet, 0, this.timeSet.length);
      timeSet[this.timeSet.length] = Calendar.getInstance();
      timeSet[this.timeSet.length].setTimeInMillis(time);
      this.timeSet = timeSet;
      this.signTime.setTimeInMillis(System.currentTimeMillis());
      this.dbReplaceSign();
      SignInform.sendMsgSignInform(this.owner);
      Iterator it = RewardHallConfigManager.getSignRewardIterator();

      while(it.hasNext()) {
         SignRewardData data = (SignRewardData)it.next();
         if (data.getCount() == timeSet.length && !this.rewardList.contains(data)) {
            SignInform.sendSignRewardState(this.owner, data.getId(), 1);
            UpdateMenu.update(this.getOwner(), 11);
            ActivityChangeDigital.pushDigital(this.getOwner(), 1, this.getCanReceiveCount());
         }
      }

   }

   public boolean drawSignReward(int id) {
      SignRewardData data = RewardHallConfigManager.getSignRewardData(id);
      if (data == null) {
         return false;
      } else if (this.timeSet.length < data.getCount()) {
         return false;
      } else if (this.rewardList.contains(data)) {
         return false;
      } else {
         List list = null;
         boolean hasVIP = this.owner.getVIPManager().getEffectBooleanValue(VIPEffectType.VE_19);
         if (hasVIP) {
            list = new ArrayList(data.getRewardList(this.owner, this.getRound()));
            ((List)list).addAll(data.getVIPRewardList(this.owner, this.getRound()));
         } else {
            list = data.getRewardList(this.owner, this.getRound());
         }

         if (this.owner.getItemManager().addItem((List)list).isOk()) {
            this.rewardList.add(data);
            this.dbReplaceSign();
            SignInform.sendSignRewardState(this.owner, id, 2);
            if (hasVIP) {
               ((List)list).clear();
            }

            list = null;
            UpdateMenu.update(this.getOwner(), 11);
            ActivityChangeDigital.pushDigital(this.getOwner(), 1, this.getCanReceiveCount());
            return true;
         } else {
            if (hasVIP) {
               ((List)list).clear();
            }

            list = null;
            return false;
         }
      }
   }

   public int getRound() {
      return this.round;
   }

   public Calendar getRoundStart() {
      return this.roundStart;
   }

   public Calendar[] getTimeSet() {
      return this.timeSet;
   }

   public CopyOnWriteArraySet getRewardList() {
      return this.rewardList;
   }

   public void dbReplaceSign() {
      try {
         WriteOnlyPacket packet = Executor.ReplaceSign.toPacket(this.getOwner().getID(), this.getRound(), this.roundStart, this.signTime, this.timeSet, this.rewardList.toArray(new SignRewardData[0]));
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void onEventSkipDay() {
      try {
         if (this.timeSet.length >= RewardHallConfigManager.getSignRoundDays()) {
            this.timeSet = new Calendar[0];
            this.rewardList = new CopyOnWriteArraySet();
            ++this.round;
            long cur = System.currentTimeMillis();
            this.roundStart.setTimeInMillis(cur);
            this.signTime.setTimeInMillis(cur);
            this.dbReplaceSign();
            SignInform.sendMsgSignInform(this.owner);
            SignInform.sendMsgSignRewardInform(this.owner);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public int getCanReceiveCount() {
      int canReceiveCount = 0;
      Iterator it = RewardHallConfigManager.getSignRewardIterator();

      while(it.hasNext()) {
         SignRewardData data = (SignRewardData)it.next();
         if (this.timeSet.length >= data.getCount() && !this.rewardList.contains(data)) {
            ++canReceiveCount;
         }
      }

      return canReceiveCount;
   }

   public void destroy() {
      this.owner = null;
      this.roundStart = null;
      this.signTime = null;
      if (this.timeSet != null) {
         for(int i = 0; i < this.timeSet.length; ++i) {
            this.timeSet[i] = null;
         }

         this.timeSet = null;
      }

      if (this.rewardList != null) {
         this.rewardList.clear();
         this.rewardList = null;
      }

   }
}

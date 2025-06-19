package com.mu.game.model.rewardhall.vitality;

import com.mu.executor.Executor;
import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.rewardhall.vitality.VitalityInform;
import com.mu.io.game.packet.imp.rewardhall.vitality.VitalityInit;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Tools;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerVitalityManager {
   private static Logger logger = LoggerFactory.getLogger(PlayerVitalityManager.class);
   private Player owner;
   private int vitality;
   private ConcurrentHashMap taskMap = Tools.newConcurrentHashMap2();
   private CopyOnWriteArraySet rewardList = new CopyOnWriteArraySet();

   public PlayerVitalityManager(Player player) {
      this.owner = player;
      Iterator it = RewardHallConfigManager.getVitalityTaskIterator();

      while(it.hasNext()) {
         this.taskMap.put((VitalityTaskData)it.next(), Integer.valueOf(0));
      }

   }

   public void init(VitalityInit packet) {
      try {
         boolean clear = this.owner.isNeedZeroClear();
         if (!clear) {
            int rate;
            if (packet.readBoolean()) {
               this.vitality = packet.readInt();
               byte[] bytes = new byte[packet.readByte()];
               packet.readBytes(bytes);

               for(rate = 0; rate < bytes.length; ++rate) {
                  VitalityRewardData vrd = RewardHallConfigManager.getVitalityRewardData(bytes[rate]);
                  this.rewardList.add(vrd);
               }
            }

            while(packet.remaining() > 0) {
               int id = packet.readByte();
               rate = packet.readInt();
               VitalityTaskData vtd = RewardHallConfigManager.getVitalityTaskData(id);
               if (vtd != null) {
                  rate = Math.max(0, Math.min(vtd.getTargetMaxRate(), rate));
                  this.taskMap.put(vtd, rate);
               }
            }
         } else {
            this.dbClearVitalityTask();
            this.dbReplaceVitality();
         }
      } catch (Exception var6) {
         logger.error("role init vitality data error!");
         var6.printStackTrace();
      }

   }

   public void addVitality(int addVitality) {
      if (addVitality >= 1) {
         int vitality = this.getVitality();
         boolean change = false;
         Iterator it = RewardHallConfigManager.getVitalityRewardIterator();

         while(it.hasNext()) {
            VitalityRewardData data = (VitalityRewardData)it.next();
            if (vitality < data.getVitality() && vitality + addVitality >= data.getVitality() && !this.rewardList.contains(data)) {
               VitalityInform.sendMsgVitalityRewardState(this.owner, data.getId(), 1);
               change = true;
            }
         }

         this.vitality += addVitality;
         VitalityInform.sendMsgCurVitality(this.owner, this.getVitality());
         this.dbReplaceVitality();
         if (change) {
            UpdateMenu.update(this.getOwner(), 11);
            ActivityChangeDigital.pushDigital(this.getOwner(), 3, this.getCanReceiveCount());
         }

      }
   }

   public boolean drawReward(int id) {
      VitalityRewardData data = RewardHallConfigManager.getVitalityRewardData(id);
      if (data == null) {
         SystemMessage.writeMessage(this.owner, 24103);
         return false;
      } else if (data.getVitality() > this.vitality) {
         SystemMessage.writeMessage(this.owner, 24104);
         return false;
      } else if (this.rewardList.contains(data)) {
         SystemMessage.writeMessage(this.owner, 24105);
         return false;
      } else if (!this.owner.getItemManager().addItem(data.getRewardList()).isOk()) {
         SystemMessage.writeMessage(this.owner, 24106);
         return false;
      } else {
         this.rewardList.add(data);
         this.dbReplaceVitality();
         VitalityInform.sendMsgVitalityRewardState(this.owner, data.getId(), 2);
         UpdateMenu.update(this.getOwner(), 11);
         ActivityChangeDigital.pushDigital(this.getOwner(), 3, this.getCanReceiveCount());
         return true;
      }
   }

   public void onTaskEvent(VitalityTaskType type, int id, int value) {
      Iterator it = RewardHallConfigManager.getVitalityTaskIterator();

      while(it.hasNext()) {
         VitalityTaskData data = (VitalityTaskData)it.next();
         if (data.getTargetType() == type && data.getTargetId() == id && ((Integer)this.taskMap.get(data)).intValue() < data.getTargetMaxRate()) {
            int rate = Math.max(0, Math.min(data.getTargetMaxRate(), ((Integer)this.taskMap.get(data)).intValue() + value));
            this.taskMap.put(data, rate);
            this.dbReplaceVitalityTask(data.getId(), rate);
            VitalityInform.sendMsgVitalityTaskRate(this.owner, data.getId(), rate);
            if (rate == data.getTargetMaxRate()) {
               this.addVitality(data.getVitality());
            }
         }
      }

   }

   public Player getOwner() {
      return this.owner;
   }

   public int getVitality() {
      return this.vitality;
   }

   public ConcurrentHashMap getTaskMap() {
      return this.taskMap;
   }

   public CopyOnWriteArraySet getRewardList() {
      return this.rewardList;
   }

   public void dbClearVitalityTask() {
      try {
         WriteOnlyPacket packet = Executor.ClearVitalityTask.toPacket(this.getOwner().getID());
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void dbReplaceVitalityTask(int id, int rate) {
      try {
         WriteOnlyPacket packet = Executor.ReplaceVitalityTask.toPacket(this.getOwner().getID(), id, rate);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void dbReplaceVitality() {
      try {
         WriteOnlyPacket packet = Executor.ReplaceVitality.toPacket(this.getOwner().getID(), this.vitality, this.rewardList.toArray(new VitalityRewardData[0]));
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void onLogin() {
      VitalityInform.sendMsgCurVitality(this.owner, this.getVitality());
   }

   public void onEventSkipDay() {
      try {
         this.vitality = 0;
         Iterator it = RewardHallConfigManager.getVitalityTaskIterator();

         while(it.hasNext()) {
            this.taskMap.put((VitalityTaskData)it.next(), Integer.valueOf(0));
         }

         this.rewardList.clear();
         this.dbClearVitalityTask();
         this.dbReplaceVitality();
         VitalityInform.sendMsgCurVitality(this.owner, this.getVitality());
         VitalityInform.sendMsgVitalityTaskInform(this.owner, this.taskMap);
         VitalityInform.sendMsgVitalityRewardInform(this.owner);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public int getCanReceiveCount() {
      int canReceiveCount = 0;
      Iterator it = RewardHallConfigManager.getVitalityRewardIterator();

      while(it.hasNext()) {
         VitalityRewardData data = (VitalityRewardData)it.next();
         if (this.vitality >= data.getVitality() && !this.rewardList.contains(data)) {
            ++canReceiveCount;
         }
      }

      return canReceiveCount;
   }

   public void destroy() {
      this.owner = null;
      if (this.taskMap != null) {
         this.taskMap.clear();
         this.taskMap = null;
      }

      if (this.rewardList != null) {
         this.rewardList.clear();
         this.rewardList = null;
      }

   }
}

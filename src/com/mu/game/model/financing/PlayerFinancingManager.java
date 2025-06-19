package com.mu.game.model.financing;

import com.mu.db.log.IngotChangeType;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.financing.FinancingInform;
import com.mu.io.game.packet.imp.financing.InitFinancing;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Tools;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerFinancingManager {
   private static Logger logger = LoggerFactory.getLogger(PlayerFinancingManager.class);
   private Player owner;
   private ConcurrentHashMap itemMap = Tools.newConcurrentHashMap2();
   private ConcurrentHashMap rewardMap = Tools.newConcurrentHashMap2();

   public PlayerFinancingManager(Player owner) {
      this.owner = owner;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void init(InitFinancing packet) {
      try {
         int itemCount = packet.readByte();

         for(int i = 0; i < itemCount; ++i) {
            int id = packet.readByte();
            int loginDay = packet.readByte();
            FinancingItemData data = FinancingConfigManager.getItemData(id);
            if (data != null) {
               int loginDay2 = this.owner.isNeedZeroClear() ? loginDay + 1 : loginDay;
               this.itemMap.put(data, loginDay2);
               if (this.owner.isNeedZeroClear()) {
                  this.dbReplaceItem(data.getId(), loginDay2);
               }
            }
         }

         while(packet.remaining() > 0) {
            int id = packet.readByte();
            long time = packet.readLong();
            FinancingItemRewardData data = FinancingConfigManager.getRewardData(id);
            if (data != null) {
               this.rewardMap.put(data, time);
            }
         }
      } catch (Exception var7) {
         logger.error("role financing init error");
         var7.printStackTrace();
      }

   }

   public void buyItem(FinancingItemData data) {
      if (1 != PlayerManager.reduceIngot(this.getOwner(), data.getPrice(), IngotChangeType.Financing, String.valueOf(data.getId()))) {
         SystemMessage.writeMessage(this.getOwner(), 1015);
      } else if (this.itemMap.get(data) != null) {
         SystemMessage.writeMessage(this.getOwner(), 23200);
      } else {
         this.itemMap.put(data, Integer.valueOf(1));
         this.dbReplaceItem(data.getId(), 1);
         FinancingInform.sendMsgState1(this.owner, data.getId(), true);
         Iterator iterator = data.getRewardIterator();

         while(iterator.hasNext()) {
            FinancingItemRewardData rdata = (FinancingItemRewardData)iterator.next();
            int state = this.getRewardState(rdata);
            if (state == 1) {
               FinancingInform.sendMsgState2(this.owner, rdata.getId(), state);
            }
         }

         FinancingInform.sendMsgBuy(this.getOwner(), data.getId());
         UpdateMenu.update(this.getOwner(), 14);
      }
   }

   public boolean receiveReward(int id) {
      FinancingItemRewardData data = FinancingConfigManager.getRewardData(id);
      if (data == null) {
         return false;
      } else if (this.rewardMap.get(data) != null) {
         SystemMessage.writeMessage(this.getOwner(), 23201);
         return false;
      } else if (this.getRewardState(data) != 1) {
         return false;
      } else if (!this.owner.getItemManager().addItem(data.getRewardList()).isOk()) {
         SystemMessage.writeMessage(this.owner, 24106);
         return false;
      } else {
         long cur = System.currentTimeMillis();
         this.rewardMap.put(data, cur);
         this.dbReplaceReward(data.getId(), cur);
         FinancingInform.sendMsgState2(this.owner, data.getId(), 2);
         UpdateMenu.update(this.getOwner(), 14);
         return true;
      }
   }

   public void onDaySkip() {
      Iterator it = this.itemMap.keySet().iterator();

      while(true) {
         FinancingItemData data;
         do {
            if (!it.hasNext()) {
               return;
            }

            data = (FinancingItemData)it.next();
         } while(data.getConditionType() != 1);

         int day = ((Integer)this.itemMap.get(data)).intValue() + 1;
         this.itemMap.put(data, day);
         Iterator iterator = data.getRewardIterator();

         while(iterator.hasNext()) {
            FinancingItemRewardData rdata = (FinancingItemRewardData)iterator.next();
            int state = this.getRewardState(rdata);
            if (state == 1) {
               FinancingInform.sendMsgState2(this.owner, rdata.getId(), state);
            }
         }

         this.dbReplaceItem(data.getId(), day);
      }
   }

   public void onPlayerLevelUp() {
      Iterator it = this.itemMap.keySet().iterator();

      while(true) {
         FinancingItemData data;
         do {
            if (!it.hasNext()) {
               UpdateMenu.update(this.getOwner(), 14);
               return;
            }

            data = (FinancingItemData)it.next();
         } while(data.getConditionType() != 2);

         Iterator iterator = data.getRewardIterator();

         while(iterator.hasNext()) {
            FinancingItemRewardData rdata = (FinancingItemRewardData)iterator.next();
            int state = this.getRewardState(rdata);
            if (state == 1) {
               FinancingInform.sendMsgState2(this.owner, rdata.getId(), state);
            }
         }
      }
   }

   public void dbReplaceItem(int itemId, int loginDay) {
      try {
         WriteOnlyPacket packet = Executor.ReplaceFinancingItem.toPacket(this.owner.getID(), itemId, loginDay);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void dbReplaceReward(int rewardId, long receiveTime) {
      try {
         WriteOnlyPacket packet = Executor.ReplaceFinancingReward.toPacket(this.owner.getID(), rewardId, receiveTime);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public boolean isBuy(FinancingItemData data) {
      return this.itemMap.get(data) != null;
   }

   public int getRewardState(FinancingItemRewardData data) {
      if (this.rewardMap.get(data) != null) {
         return 2;
      } else {
         Integer loginDay = (Integer)this.itemMap.get(data.getItemData());
         if (loginDay == null) {
            return 0;
         } else {
            switch(data.getItemData().getConditionType()) {
            case 1:
               return loginDay.intValue() >= data.getConditionValue() ? 1 : 0;
            case 2:
               return this.getOwner().getLevel() >= data.getConditionValue() ? 1 : 0;
            default:
               return 0;
            }
         }
      }
   }

   public int getRiceiveNumber() {
      int num = 0;
      Iterator it = this.itemMap.keySet().iterator();

      while(it.hasNext()) {
         FinancingItemData data = (FinancingItemData)it.next();
         Iterator iterator = data.getRewardIterator();

         while(iterator.hasNext()) {
            FinancingItemRewardData rdata = (FinancingItemRewardData)iterator.next();
            int state = this.getRewardState(rdata);
            if (state == 1) {
               ++num;
            }
         }
      }

      return num;
   }

   public void destroy() {
      this.owner = null;
      if (this.itemMap != null) {
         this.itemMap.clear();
         this.itemMap = null;
      }

      if (this.rewardMap != null) {
         this.rewardMap.clear();
         this.rewardMap = null;
      }

   }
}

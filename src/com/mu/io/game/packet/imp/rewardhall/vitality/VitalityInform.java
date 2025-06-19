package com.mu.io.game.packet.imp.rewardhall.vitality;

import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.rewardhall.RewardItemData;
import com.mu.game.model.rewardhall.vitality.PlayerVitalityManager;
import com.mu.game.model.rewardhall.vitality.VitalityRewardData;
import com.mu.game.model.rewardhall.vitality.VitalityTaskData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class VitalityInform extends ReadAndWritePacket {
   public VitalityInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      sendMsgVitalityTaskInform(this.getPlayer(), this.getPlayer().getVitalityManager().getTaskMap());
      sendMsgVitalityRewardInform(this.getPlayer());
   }

   public static void sendMsgCurVitality(Player player, int curVitality) {
      try {
         VitalityInform packet = new VitalityInform(46201, (byte[])null);
         packet.writeShort(curVitality);
         packet.writeShort(RewardHallConfigManager.getMaxVitality());
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendMsgVitalityTaskInform(Player player, ConcurrentHashMap taskMap) {
      try {
         VitalityInform packet = new VitalityInform(46202, (byte[])null);
         packet.writeByte(RewardHallConfigManager.getVitalityTaskSize());
         Iterator it = RewardHallConfigManager.getVitalityTaskIterator();

         while(it.hasNext()) {
            VitalityTaskData data = (VitalityTaskData)it.next();
            Integer rate = (Integer)taskMap.get(data);
            packet.writeByte(data.getId());
            packet.writeUTF(data.getName());
            packet.writeShort(data.getVitality());
            packet.writeInt(rate == null ? 0 : rate.intValue());
            packet.writeInt(data.getTargetMaxRate());
            packet.writeBoolean(data.isHasEnter());
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void sendMsgVitalityTaskRate(Player player, int id, int rate) {
      try {
         VitalityInform packet = new VitalityInform(46203, (byte[])null);
         packet.writeByte(id);
         packet.writeInt(rate);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendMsgVitalityRewardInform(Player player) {
      try {
         PlayerVitalityManager pvm = player.getVitalityManager();
         CopyOnWriteArraySet rewardList = pvm.getRewardList();
         VitalityInform packet = new VitalityInform(46205, (byte[])null);
         packet.writeByte(RewardHallConfigManager.getVitalityRewardSize());
         Iterator it = RewardHallConfigManager.getVitalityRewardIterator();

         while(it.hasNext()) {
            VitalityRewardData data = (VitalityRewardData)it.next();
            packet.writeByte(data.getId());
            packet.writeShort(data.getVitality());
            List list = data.getRewardList();
            packet.writeByte(list.size());
            Iterator itt = list.iterator();

            while(itt.hasNext()) {
               GetItemStats.writeItem(((RewardItemData)itt.next()).getItem(), packet);
            }

            packet.writeByte(rewardList.contains(data) ? 2 : (pvm.getVitality() >= data.getVitality() ? 1 : 0));
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void sendMsgVitalityRewardState(Player player, int id, int rate) {
      try {
         VitalityInform packet = new VitalityInform(46206, (byte[])null);
         packet.writeByte(id);
         packet.writeByte(rate);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}

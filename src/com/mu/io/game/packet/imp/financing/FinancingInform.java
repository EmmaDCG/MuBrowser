package com.mu.io.game.packet.imp.financing;

import com.mu.game.model.financing.FinancingConfigManager;
import com.mu.game.model.financing.FinancingItemData;
import com.mu.game.model.financing.FinancingItemRewardData;
import com.mu.game.model.financing.PlayerFinancingManager;
import com.mu.game.model.rewardhall.RewardItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;
import java.util.List;

public class FinancingInform extends ReadAndWritePacket {
   public FinancingInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      sendMsgInform(this.getPlayer());
   }

   public static void sendMsgInform(Player player) {
      try {
         PlayerFinancingManager pfm = player.getFinancingManager();
         FinancingInform packet = new FinancingInform(42001, (byte[])null);
         packet.writeByte(FinancingConfigManager.getItemSize());
         Iterator it = FinancingConfigManager.getItemIterator();

         while(it.hasNext()) {
            FinancingItemData data = (FinancingItemData)it.next();
            packet.writeByte(data.getId());
            packet.writeUTF(data.getName());
            packet.writeShort(data.getImage1());
            packet.writeShort(data.getImage2());
            packet.writeBoolean(pfm.isBuy(data));
            packet.writeByte(data.getRewardSize());
            Iterator iterator = data.getRewardIterator();

            while(iterator.hasNext()) {
               FinancingItemRewardData rdata = (FinancingItemRewardData)iterator.next();
               packet.writeByte(rdata.getId());
               packet.writeShort(rdata.getImage());
               packet.writeByte(pfm.getRewardState(rdata));
               List list = rdata.getRewardList();
               packet.writeByte(list.size());
               Iterator itt = list.iterator();

               while(itt.hasNext()) {
                  GetItemStats.writeItem(((RewardItemData)itt.next()).getItem(), packet);
               }
            }
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static void sendMsgState1(Player player, int id, boolean isBuy) {
      try {
         FinancingInform packet = new FinancingInform(42002, (byte[])null);
         packet.writeByte(id);
         packet.writeBoolean(isBuy);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendMsgState2(Player player, int id, int state) {
      try {
         FinancingInform packet = new FinancingInform(42003, (byte[])null);
         packet.writeByte(id);
         packet.writeByte(state);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendMsgBuy(Player player, int id) {
      try {
         FinancingInform packet = new FinancingInform(42004, (byte[])null);
         packet.writeByte(id);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}

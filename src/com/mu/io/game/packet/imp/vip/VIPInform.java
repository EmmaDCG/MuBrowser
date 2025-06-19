package com.mu.io.game.packet.imp.vip;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.vip.PlayerVIPManager;
import com.mu.game.model.vip.VIPConfigManager;
import com.mu.game.model.vip.VIPData;
import com.mu.game.model.vip.VIPLevel;
import com.mu.game.model.vip.effect.VIPEffect;
import com.mu.game.model.vip.effect.VIPEffectValue;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;

public class VIPInform extends WriteOnlyPacket {
   public VIPInform(int code) {
      super(code);
   }

   public static void writeVIPConfig(WriteOnlyPacket packet) {
      try {
         packet.writeByte(VIPConfigManager.getVIPSize());
         Iterator it = VIPConfigManager.getVIPIterator();

         while(it.hasNext()) {
            VIPData data = (VIPData)it.next();
            packet.writeByte(data.getId());
            packet.writeUTF(data.getName());
            packet.writeShort(data.getImage());
            packet.writeByte(4);
            packet.writeUTF(data.getSlStr());
            packet.writeUTF(data.getCfStr());
            packet.writeUTF(data.getQlStr());
            packet.writeUTF(data.getMwStr());
            packet.writeUTF(data.getXxStr());
            packet.writeByte(data.getExp());
            packet.writeInt(data.getPrice());
            packet.writeUTF(data.getTimeStr());
         }

         packet.writeByte(VIPConfigManager.getEffectSize());
         it = VIPConfigManager.getEffectIterator();

         while(it.hasNext()) {
            VIPEffect ve = (VIPEffect)it.next();
            packet.writeUTF(ve.getName());

            for(VIPLevel vl = VIPConfigManager.getLevelHead(); vl != null; vl = vl.getNext()) {
               VIPEffectValue vev = ve.getValue(vl);
               packet.writeByte(vev.getShowType());
               switch(vev.getShowType()) {
               case 1:
                  packet.writeBoolean(vev.isShowBool());
                  break;
               case 2:
                  packet.writeUTF(vev.getShowStr());
               }
            }
         }

         packet.writeShort(10);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendMsgVIPLevel(Player player) {
      try {
         VIPInform inform = new VIPInform(41001);
         boolean active = !player.getVIPManager().isTimeOut();
         inform.writeBoolean(active);
         if (active) {
            inform.writeByte(player.getVIPLevel());
            PlayerVIPManager pvm = player.getVIPManager();
            inform.writeByte(pvm.getData().getId());
            inform.writeDouble((double)pvm.getOffTime());
            inform.writeInt(player.getVIPExp() / 10);
            inform.writeInt(pvm.getMaxExp() / 10);
         }

         inform.writeInt(VIPConfigManager.getLevel(player.getVIPLevel()).getMaxExp());
         player.writePacket(inform);
         inform.destroy();
         inform = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}

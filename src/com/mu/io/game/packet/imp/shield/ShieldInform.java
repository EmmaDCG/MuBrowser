package com.mu.io.game.packet.imp.shield;

import com.mu.game.model.shield.PlayerShieldManager;
import com.mu.game.model.shield.ShieldLevel;
import com.mu.game.model.shield.ShieldRank;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import java.util.Iterator;
import java.util.List;

public class ShieldInform extends ReadAndWritePacket {
   public ShieldInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      sendMsgShieldInform(this.getPlayer().getShieldManager());
   }

   public static void sendMsgShieldInform(PlayerShieldManager psm) {
      try {
         ShieldInform packet = new ShieldInform(45202, (byte[])null);
         ShieldLevel lv = psm.getLevel();
         ShieldRank rk = psm.getRank();
         Player player = psm.getOwner();
         packet.writeShort(lv.getLevel());
         packet.writeShort(rk.getRank());
         packet.writeShort(rk.getEffect());
         int maxSD = psm.getOwner().getMaxSD();
         packet.writeInt(maxSD);
         packet.writeInt(lv.getZDL() + rk.getZDL());
         packet.writeDouble((double)lv.getExpend());
         packet.writeDouble((double)player.getMoney());
         ShieldRank nextRk = rk.getNext();
         List propertyList = rk.getPropertyList();
         packet.writeByte(propertyList.size());
         Iterator it = propertyList.iterator();

         while(it.hasNext()) {
            FinalModify att = (FinalModify)it.next();
            PlayerAttributes.writeStat(att.getStat(), (long)(att.getStat() == StatEnum.SD_RECOVER ? psm.getOwner().getSdRecover() : att.getValue()), att.isPercent(), packet);
            if (nextRk != null) {
               packet.writeDouble((double)(((FinalModify)nextRk.getPropertyMap().get(att.getStat())).getValue() - att.getValue()));
            } else {
               packet.writeDouble(-1.0D);
            }
         }

         packet.writeByte(rk.getStar());
         GetItemStats.writeItem(rk.getExpendItem(), packet);
         packet.writeShort(psm.getRankExp());
         packet.writeShort(rk.getLimit());
         psm.getOwner().writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public static void sendMsgShieldRankEXP(Player player, boolean upstar, int maxExp, int curExp) {
      try {
         ShieldInform packet = new ShieldInform(45204, (byte[])null);
         packet.writeBoolean(upstar);
         if (!upstar) {
            packet.writeShort(curExp);
            packet.writeShort(maxExp);
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}

package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PushGangBaseInfo extends WriteOnlyPacket {
   public PushGangBaseInfo() {
      super(10600);
   }

   public static PushGangBaseInfo createBaseInfo(Gang gang) {
      PushGangBaseInfo pi = new PushGangBaseInfo();

      try {
         writeInfo(pi, gang);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return pi;
   }

   private static void writeInfo(PushGangBaseInfo pi, Gang gang) throws Exception {
      pi.writeDouble((double)gang.getId());
      pi.writeByte(gang.getFlagId());
      pi.writeUTF(gang.getName());
      pi.writeByte(gang.getLevel());
      pi.writeUTF(GangManager.getLevelData(gang.getLevel()).getName());
      pi.writeDouble((double)gang.getMasterId());
      pi.writeUTF(gang.getMasterName());
      pi.writeByte(gang.getMemberSize());
      pi.writeByte(GangManager.getLevelData(gang.getLevel()).getMaxMember());
   }

   public static void pushInfo(Player player) {
      try {
         Gang gang = player.getGang();
         GangMember member = GangManager.getMember(player.getID());
         if (gang == null || member == null) {
            return;
         }

         PushGangBaseInfo pi = new PushGangBaseInfo();
         writeInfo(pi, gang);
         player.writePacket(pi);
         pi.destroy();
         pi = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}

package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangLevelData;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class GangLevelUp extends ReadAndWritePacket {
   public GangLevelUp(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GangLevelUp() {
      super(10620, (byte[])null);
   }

   public static void pushLevelUpFail(Gang gang, Player player) {
      GangLevelUp gu = new GangLevelUp();

      try {
         gu.writeBoolean(false);
         player.writePacket(gu);
         gu.destroy();
         gu = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void pushLevelUpSuccess(Gang gang) {
      GangLevelUp gu = new GangLevelUp();

      try {
         gu.writeBoolean(true);
         gu.writeByte(gang.getLevel());
         GangLevelData data = GangManager.getLevelData(gang.getLevel());
         gu.writeByte(data.getMaxMember());
         gang.broadcast(gu);
         gu.destroy();
         gu = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      gang.doOperation(player, 10);
   }
}
